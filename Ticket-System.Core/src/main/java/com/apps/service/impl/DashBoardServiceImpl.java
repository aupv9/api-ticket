package com.apps.service.impl;

import com.apps.contants.Role;
import com.apps.contants.Utilities;
import com.apps.domain.entity.Employee;
import com.apps.domain.entity.UserRole;
import com.apps.mybatis.mysql.EmployeeRepository;
import com.apps.response.entity.EmployeeDto;
import com.apps.response.entity.PercentCoverRoom;
import com.apps.response.entity.RevenueEmployee;
import com.apps.service.*;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {


    private final EmployeeRepository employeeRepository;

    private final RoleService roleService;

    private final OrdersServiceImpl ordersService;

    private final UserService userService;

    private final ShowTimesDetailService showTimesDetailService;

    private final SeatService seatService;

    private final RoomService roomService;

    @Override
//    @Cacheable(value = "DashBoardService" ,key = "'findAllRevenue_'+#limit+'-'+#offset+" +
//            "'-+#sort+'-'+#order+'-''+#roleId+'-'+#theaterId+'-'+#date", unless = "#result == null")
    public List<RevenueEmployee> findAllRevenue(Integer limit, Integer offset, String sort,
                                                String order, Integer roleId, Integer theaterId,String date) {

        return this.addRevenue(
                    this.addRole(
                        this.employeeRepository.findAll(limit,offset,sort,order,roleId,theaterId)),date);
    }


    @Override
    public int findCountAll(Integer roleId, Integer theaterId) {
        return 0;
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    @Cacheable(cacheNames = "PercentService",key = "'getPercentCoverSeatOnTheater_'" +
            "+#date+'-'+#theater",unless = "#result == null ")
    public List<PercentCoverRoom> getPercentCoverSeatOnTheater(String date,Integer theater) {
        var listPercentCoverRoom = new ArrayList<PercentCoverRoom>();
        var listRoom = this.roomService.findByTheater(theater);
        for (var room : listRoom){
            var percentCoverRoom = new PercentCoverRoom();
            percentCoverRoom.setId(room.getName());
            percentCoverRoom.setLabel(room.getName());
            var listShow =
                    this.showTimesDetailService.findShowStartByDay(Utilities.convertIsoToDate(date),
                    room.getId());
            double percentPerShow = 0.d;
            for (var show: listShow){
                double countSeatByRoom = this.roomService.countSeatById(room.getId());
                double countAvailable = this.seatService.countSeatAvailable(show.getId(),room.getId());
                double remainSeat = countSeatByRoom - countAvailable;
                if(countSeatByRoom <= 0){
                    percentPerShow += 0;
                }else{
                    var percentCover = ((remainSeat / countSeatByRoom ));
                   ;
                    percentPerShow +=  Double.parseDouble(df.format(percentCover));
                }
            }
            int showCount = listShow.size();
            double mediumPercentCoverPerRoom = 0.d;
            if(showCount > 0){
                 mediumPercentCoverPerRoom = Double.parseDouble(df.format(percentPerShow / showCount));
                percentCoverRoom.setValue(mediumPercentCoverPerRoom);
            }else{
                percentCoverRoom.setValue(0);
            }
            listPercentCoverRoom.add(percentCoverRoom);
        }
        return listPercentCoverRoom;
    }

    public List<EmployeeDto> addRole(List<Employee> employees){
        var listEmployee = new ArrayList<EmployeeDto>();
        employees.forEach(employee -> {
            var employeeDto = EmployeeDto.builder()
                    .id(employee.getId()).createdAt(employee.getCreatedAt())
                    .createdBy(employee.getCreatedBy()).endsAt(employee.getEndsAt())
                    .notes(employee.getNotes()).updatedAt(employee.getUpdatedAt())
                    .updatedBy(employee.getUpdatedBy()).theaterId(employee.getTheaterId())
                    .status(employee.getStatus()).userId(employee.getUserId())
                    .startsAt(employee.getStartsAt()).endsAt(employee.getEndsAt())
                    .build();
            employeeDto.setRoleIds(this.getRolesByUser(employee.getUserId()));
            listEmployee.add(employeeDto);
        });
        return listEmployee;
    }

    @Cacheable(cacheNames = "RoleService",key = "'getRolesByUser_'+#userId",unless = "#result == null ")
    public List<Integer> getRolesByUser(Integer userId){
        return this.roleService.findUserRoleById(userId)
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }


    private boolean filterEmployee(List<Integer> roles){
        for (Integer role : roles) {
            if(this.roleService.findRoleById(role).getCode().equals(Role.ADMIN.getName())
                || this.roleService.findRoleById(role).getCode().equals(Role.SENIOR_MANAGER.getName())){
                return true;
            }
        }
        return false;
    }

    public List<RevenueEmployee> addRevenue(List<EmployeeDto> employeeList,String date){
        var listEmployee = new ArrayList<RevenueEmployee>();
        employeeList.stream().filter(employeeDto -> !filterEmployee(employeeDto.getRoleIds())).forEach(item ->{
            var user = this.userService.findById(item.getUserId());
            var employee = RevenueEmployee.builder().avatar(user.getPhoto())
                    .id(item.getId()).createdAt(item.getCreatedAt()).fullName(user.getFullName())
                    .createdBy(item.getCreatedBy()).endsAt(item.getEndsAt())
                    .notes(item.getNotes()).updatedAt(item.getUpdatedAt())
                    .updatedBy(item.getUpdatedBy()).theaterId(item.getTheaterId())
                    .status(item.getStatus()).userId(item.getUserId()).online(user.getCurrentLogged())
                    .startsAt(item.getStartsAt()).endsAt(item.getEndsAt()).roleIds(item.getRoleIds())
                    .build();
            var orders = this.ordersService.findAllByCreationAndCreated(employee.getUserId(),
                    Utilities.convertIsoToDate(date));
            employee.setCountOrder(orders.size());
            double revenue = 0.d;
            for (var order : orders){
                revenue += this.ordersService.getTotalOrder(order);
            }
            employee.setRevenue(revenue);
            listEmployee.add(employee);
        });
        return listEmployee;
    }

}
