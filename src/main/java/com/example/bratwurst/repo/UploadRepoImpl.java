package com.example.bratwurst.repo;

import com.example.bratwurst.model.File;
import com.example.bratwurst.service.UploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository("UploadRepo")
public class UploadRepoImpl implements UploadRepo {

    Logger log = Logger.getLogger(UploadRepoImpl.class.getName());

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public File saveFile(File file) {
        String sql = "INSERT INTO bratwurst.files (filename, user) VALUES (?,?)";
        String filename = file.getFilename();
        int user = file.getUser();

        this.jdbc.update(sql, filename, user);

        return file;
    }

    @Override
    public List<File> getFiles(int user) {String sql = "SELECT * FROM bratwurst.files WHERE user = ?";
        return this.jdbc.query(sql, new ResultSetExtractor<List<File>>() {

            @Override
            public List<File> extractData(ResultSet rs) throws SQLException, DataAccessException {
                int id, user;
                String filename;
                Timestamp upload_time;
                ArrayList<File> holidays = new ArrayList<>();

                while (rs.next()) {
                    id = rs.getInt("id");
                    filename = rs.getString("filename");
                    upload_time = rs.getTimestamp("upload_time");
                    user = rs.getInt("user");

                    holidays.add(new File(id, filename, upload_time, user));
                }
                return holidays;
            }
        }, user);
    }

    @Override
    public File getFile(int id){
        String sql = "SELECT * FROM bratwurst.files WHERE id = ?";
        RowMapper<File> rowMapper = new BeanPropertyRowMapper<>(File.class);

        File file = jdbc.queryForObject(sql,rowMapper, id );

        return file;
    }

    @Override
    public String deleteFile(int id) {
        String filename = getFile(id).getFilename();

        String sql = "DELETE FROM bratwurst.files WHERE id = ?";
        this.jdbc.update(sql, id );

        return filename;
    }
}
