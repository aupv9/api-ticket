package com.apps.service.impl;

import com.apps.contants.Utilities;
import com.apps.domain.entity.Cast;
import com.apps.domain.entity.Movie;
import com.apps.domain.entity.Tag;
import com.apps.domain.repository.MovieCustomRepository;
import com.apps.mybatis.mysql.MovieRepository;
import com.apps.request.MovieDto;
import com.apps.response.MovieResponse;
import com.apps.service.CastService;
import com.apps.service.MediaService;
import com.apps.service.MovieService;
import com.apps.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final MovieCustomRepository movieCustomRepository;

    private final CastService castService;

    private final TagService tagService;

    private final MediaService mediaService;

    @Override
    public List<Movie> findByLocationAndDate(int location, String date) {
        return movieRepository.findByLocationAndDate(location,date);
    }

    @Override
    @Cacheable(cacheNames = "MovieService", key = "'findAllMovie_'+#page+'-'+#size+'-'+#search+'-'+#sort+'-'+#order",
            unless = "#result == null ")
    public List<MovieResponse> findAll(int page, int size,String search, String sort, String order) {
        var movies = movieRepository.findAll(page * size, size,search,sort,order);
        return this.addInfoForMovie(movies);
    }

    private List<Cast> findCast(Integer movie){
        var listCast = this.movieRepository.findAllCastInMovie(movie);
        var newListMovie = new ArrayList<Cast>();
        for (var castId : listCast){
            var cast = this.castService.findById(castId);
            newListMovie.add(cast);
        }
        return newListMovie;
    }
    private List<MovieResponse> addInfoForMovie(List<Movie> movieList){
        var newListMovie = new ArrayList<MovieResponse>();

        movieList.forEach(movie -> {
            var movieDto = MovieResponse.builder()
                    .id(movie.getId()).casts(this.findCast(movie.getId()))
                    .genre(movie.getGenre()).name(movie.getName()).durationMin(movie.getDurationMin())
                    .releasedDate(movie.getReleasedDate()).thumbnail(movie.getThumbnail())
                    .trailerUrl(movie.getTrailerUrl())
                    .build();
            newListMovie.add(movieDto);
        });
        return newListMovie;

    }
    @Override
    @Cacheable(cacheNames = "MovieService", key = "'findAllCountMovie_'+#search",
            unless = "#result == null ")
    public int findAllCount(String search) {
        return movieRepository.findAllCount(search);
    }

    @Override
    @Cacheable(cacheNames = "MovieService", key = "'findByIdMovie_'+#id",
            unless = "#result == null ")
    public Movie findById(int id) {
        return movieRepository.findById(id);
    }

    @Override
    public Movie findByName(String name) {
        return this.movieRepository.findByName(name);
    }

    @Override
    public Movie findByCode(String code) {
        return this.movieRepository.findByCode(code);
    }

    @Override
    @CacheEvict(value = "MovieService",allEntries = true,key = "'findByIdMovie_'+#movie.id")
    public int update(Movie movie) {
        Movie movie1 = findById(movie.getId());
        movie1.setName(movie.getName());
        movie1.setThumbnail(movie.getThumbnail());
        movie1.setImage(movie.getImage());
        int result = this.movieRepository.update(movie1);
        return result;
    }

    @Override
    public int insert(Movie movie) throws SQLException {
        String sql = "INSERT INTO MOVIE(name,thumbnail,image,genre," +
                "released_date,trailer_url,duration_min)" +
                " VALUES(?,?,?,?,?,?,?)";
        return this.movieCustomRepository.insert(movie,sql);
    }

    @Override
    @CacheEvict(value = "MovieService",allEntries = true,key = "'findByIdMovie_'+#id")
    public void delete(Integer id) {
        Movie movie1 = findById(id);
        this.movieRepository.delete(id);
    }

    @Override
    public List<Movie> findAllCurrentWeek() {
        return this.movieRepository.findAllCurrentWeek(Utilities.startOfWeek("yyyy-MM-dd")
                ,Utilities.addDate(30));
    }

    @Override
    public List<Movie> findAllComingSoon() {
        String date = Utilities.currentWeekEndDate();
        return this.movieRepository.findAllComingSoon(date);
    }

    @Override
    @CacheEvict(value = "MovieService",allEntries = true)
    public int insertMulti(MovieDto movieDto) throws SQLException {
        var movie = Movie.builder().name(movieDto.getName())
                        .thumbnail(movieDto.getThumbnail()).genre(movieDto.getGenre())
                        .releasedDate(movieDto.getReleasedDate()).genre(movieDto.getGenre())
                        .trailerUrl(movieDto.getTrailerUrl())
                        .durationMin(movieDto.getDurationMin())
                        .build();
        int idMovie = this.insert(movie);

        if(movieDto.getCasts() != null){
            var listCast = movieDto.getCasts().split(",");
            for (var cast : listCast){
                var castI = this.castService.findByName(cast);
                if (castI != null) {
                    this.movieRepository.insertMovieCast(idMovie,castI.getId());
                }else{
                    var newCast = Cast.builder()
                            .name(cast).profile("").role("")
                            .build();
                    this.castService.insert(newCast);
                }
            }
        }

//        var oldPhoto = this.mediaService.findByPath(movieDto.getPhoto());
//        if (oldPhoto != null) {
//            this.movieRepository.insertMovieMedia(idMovie,oldPhoto.getId());
//        }else{
//            var media = Media.builder()
//                    .name("").creationDate(Utilities.getCurrentTime())
//                    .path(movieDto.getPhoto())
//                    .build();
//            this.mediaService.insert(media);
//        }

        if(movieDto.getTags() != null){
            var listTag = movieDto.getTags().split(",");
            for (var tag : listTag){
                var  tagOld= this.tagService.findByName(tag);
                if (tagOld != null) {
                    this.movieRepository.insertMovieTag(idMovie,tag);
                }else{
                    var tagNew = Tag.builder()
                            .name(tag)
                            .build();
                    this.tagService.insert(tagNew);
                }
            }
        }
        return idMovie;
    }
}
