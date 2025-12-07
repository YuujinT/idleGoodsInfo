package com.example.idlegoodsinfo.dao;

import com.example.idlegoodsinfo.entity.Listing;

import java.util.List;
import java.util.Optional;

public interface ListingDao {
    Listing save(Listing listing);

    void update(Listing listing);

    void delete(Long listingId, Long ownerId);

    Optional<Listing> findById(Long id);

    List<Listing> findByOwner(Long ownerId);

    List<Listing> searchByKeyword(String keyword);

    List<Listing> findAll();
}

