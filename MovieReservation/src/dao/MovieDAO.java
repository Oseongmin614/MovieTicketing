package dao.impl;

import dao.MovieDAO;
import vo.MovieVO;
import vo.ShowVO;
import vo.ShowSeatVO;
import vo.SeatVO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAOImpl implements MovieDAO {
    private DataSource dataSource;
    
    // SQL 쿼리
    private static final String SELECT_ALL_MOVIES = "SELECT * FROM MOVIE";
    private static final String SELECT_MOVIE_BY_ID = "SELECT * FROM MOVIE WHERE MOVIE_ID = ?";
    private static final String SELECT_SHOWS_BY_MOVIE = "SELECT * FROM SHOW WHERE MOVIE_ID = ?";
    private static final String SELECT_SHOW_SEATS = 
            "SELECT ss.*, s.ROW_LABEL, s.COL_NUM, s.SEAT_CODE " +
            "FROM SHOW_SEAT ss JOIN SEAT s ON ss.SEAT_ID = s.SEAT_ID " +
            "WHERE ss.SHOW_ID = ?";
    private static final String SELECT_MOVIES_BY_TITLE = "SELECT * FROM MOVIE WHERE TITLE LIKE ?";
    private static final String INSERT_MOVIE = 
            "INSERT INTO MOVIE (TITLE, DURATION_MIN, DESCRIPTION, REG_DATE) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_MOVIE = 
            "UPDATE MOVIE SET TITLE = ?, DURATION_MIN = ?, DESCRIPTION = ? WHERE MOVIE_ID = ?";
    private static final String DELETE_MOVIE = "DELETE FROM MOVIE WHERE MOVIE_ID = ?";
    
    public MovieDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public List<MovieVO> findAll() {
        List<MovieVO> movies = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_MOVIES)) {
            
            while (rs.next()) {
                movies.add(extractMovieFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return movies;
    }
    
    @Override
    public MovieVO findById(int movieId) {
        MovieVO movie = null;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_MOVIE_BY_ID)) {
            
            ps.setInt(1, movieId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    movie = extractMovieFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return movie;
    }
    
    @Override
    public List<ShowVO> findShowsByMovieId(int movieId) {
        List<ShowVO> shows = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SHOWS_BY_MOVIE)) {
            
            ps.setInt(1, movieId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    shows.add(extractShowFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return shows;
    }
    
    @Override
    public List<ShowSeatVO> findSeatsByShowId(int showId) {
        List<ShowSeatVO> seats = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SHOW_SEATS)) {
            
            ps.setInt(1, showId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seats.add(extractShowSeatFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return seats;
    }
    
    @Override
    public void create(MovieVO movie) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_MOVIE, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDurationMin());
            ps.setString(3, movie.getDescription());
            ps.setDate(4, new java.sql.Date(movie.getRegDate().getTime()));
            
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    movie.setMovieId(generatedKeys.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void update(MovieVO movie) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_MOVIE)) {
            
            ps.setString(1, movie.getTitle());
            ps.setInt(2, movie.getDurationMin());
            ps.setString(3, movie.getDescription());
            ps.setInt(4, movie.getMovieId());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void delete(int movieId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_MOVIE)) {
            
            ps.setInt(1, movieId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<MovieVO> findMoviesByTitle(String title) {
        List<MovieVO> movies = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_MOVIES_BY_TITLE)) {
            
            ps.setString(1, "%" + title + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movies.add(extractMovieFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return movies;
    }
    
    @Override
    public List<MovieVO> getAllMovies() {
        List<MovieVO> movies = findAll();
        
        // 각 영화에 대한 상영정보 및 좌석정보 로드
        for (MovieVO movie : movies) {
            List<ShowVO> shows = findShowsByMovieId(movie.getMovieId());
            
            for (ShowVO show : shows) {
                List<ShowSeatVO> seats = findSeatsByShowId(show.getShowId());
                show.setShowSeatList(seats);
            }
            
            movie.setShowList(shows);
        }
        
        return movies;
    }
    
    @Override
    public MovieVO getMovieWithDetails(int movieId) {
        MovieVO movie = findById(movieId);
        
        if (movie != null) {
            List<ShowVO> shows = findShowsByMovieId(movieId);
            
            for (ShowVO show : shows) {
                List<ShowSeatVO> seats = findSeatsByShowId(show.getShowId());
                show.setShowSeatList(seats);
            }
            
            movie.setShowList(shows);
        }
        
        return movie;
    }
    
    // ResultSet에서 객체 추출 헬퍼 메서드
    private MovieVO extractMovieFromResultSet(ResultSet rs) throws SQLException {
        MovieVO movie = new MovieVO();
        movie.setMovieId(rs.getInt("MOVIE_ID"));
        movie.setTitle(rs.getString("TITLE"));
        movie.setDurationMin(rs.getInt("DURATION_MIN"));
        movie.setDescription(rs.getString("DESCRIPTION"));
        movie.setRegDate(rs.getDate("REG_DATE"));
        return movie;
    }
    
    private ShowVO extractShowFromResultSet(ResultSet rs) throws SQLException {
        ShowVO show = new ShowVO();
        show.setShowId(rs.getInt("SHOW_ID"));
        show.setMovieId(rs.getInt("MOVIE_ID"));
        show.setShowTime(rs.getString("SHOW_TIME"));
        return show;
    }
    
    private ShowSeatVO extractShowSeatFromResultSet(ResultSet rs) throws SQLException {
        ShowSeatVO showSeat = new ShowSeatVO();
        showSeat.setShowSeatId(rs.getInt("SHOW_SEAT_ID"));
        showSeat.setShowId(rs.getInt("SHOW_ID"));
        showSeat.setSeatId(rs.getInt("SEAT_ID"));
        showSeat.setReserved(rs.getBoolean("IS_RESERVED"));
        return showSeat;
    }
}
