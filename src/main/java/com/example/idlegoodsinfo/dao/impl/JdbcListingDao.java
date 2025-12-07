package com.example.idlegoodsinfo.dao.impl;

import com.example.idlegoodsinfo.config.DataSourceManager;
import com.example.idlegoodsinfo.dao.ListingDao;
import com.example.idlegoodsinfo.entity.Listing;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcListingDao implements ListingDao {
    private final DataSource dataSource;

    public JdbcListingDao() {
        this.dataSource = DataSourceManager.getDataSource();
    }

    @Override
    public Listing save(Listing listing) {
        String sql = "INSERT INTO listings(user_id, title, description, quantity) VALUES(?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, listing.getUserId());
            ps.setString(2, listing.getTitle());
            ps.setString(3, listing.getDescription());
            ps.setInt(4, listing.getQuantity());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    listing.setId(rs.getLong(1));
                    listing.setCreatedAt(Instant.now());
                    listing.setUpdatedAt(listing.getCreatedAt());
                }
            }
            return listing;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save listing", e);
        }
    }

    @Override
    public void update(Listing listing) {
        String sql = "UPDATE listings SET title = ?, description = ?, quantity = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, listing.getTitle());
            ps.setString(2, listing.getDescription());
            ps.setInt(3, listing.getQuantity());
            ps.setLong(4, listing.getId());
            ps.setLong(5, listing.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update listing", e);
        }
    }

    @Override
    public void delete(Long listingId, Long ownerId) {
        String sql = "DELETE FROM listings WHERE id = ? AND user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, listingId);
            ps.setLong(2, ownerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete listing", e);
        }
    }

    @Override
    public Optional<Listing> findById(Long id) {
        String sql = baseQuery() + " WHERE l.id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find listing by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Listing> findByOwner(Long ownerId) {
        String sql = baseQuery() + " WHERE l.user_id = ? ORDER BY l.updated_at DESC";
        List<Listing> results = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find listings by owner", e);
        }
        return results;
    }

    @Override
    public List<Listing> searchByKeyword(String keyword) {
        String sql = baseQuery() + " WHERE l.title LIKE ? OR l.description LIKE ? ORDER BY l.updated_at DESC";
        List<Listing> results = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search listings", e);
        }
        return results;
    }

    @Override
    public List<Listing> findAll() {
        String sql = baseQuery() + " ORDER BY l.updated_at DESC";
        List<Listing> results = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all listings", e);
        }
        return results;
    }

    private String baseQuery() {
        return "SELECT l.id, l.user_id, l.title, l.description, l.quantity, l.created_at, l.updated_at, u.username " +
               "FROM listings l JOIN users u ON l.user_id = u.id";
    }

    private Listing mapRow(ResultSet rs) throws SQLException {
        Listing listing = new Listing();
        listing.setId(rs.getLong("id"));
        listing.setUserId(rs.getLong("user_id"));
        listing.setTitle(rs.getString("title"));
        listing.setDescription(rs.getString("description"));
        listing.setQuantity(rs.getInt("quantity"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (createdAt != null) {
            listing.setCreatedAt(createdAt.toInstant());
        }
        if (updatedAt != null) {
            listing.setUpdatedAt(updatedAt.toInstant());
        }
        listing.setOwnerUsername(rs.getString("username"));
        return listing;
    }
}

