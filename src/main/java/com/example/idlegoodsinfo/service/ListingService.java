package com.example.idlegoodsinfo.service;

import com.example.idlegoodsinfo.dao.ListingDao;
import com.example.idlegoodsinfo.dao.impl.JdbcListingDao;
import com.example.idlegoodsinfo.entity.Listing;

import java.util.List;
import java.util.Optional;

public class ListingService {
    private final ListingDao listingDao;

    public ListingService() {
        this.listingDao = new JdbcListingDao();
    }

    public ListingService(ListingDao listingDao) {
        this.listingDao = listingDao;
    }

    public Listing createListing(Long userId, String title, String description, int quantity) {
        Listing listing = new Listing();
        listing.setUserId(userId);
        listing.setTitle(title);
        listing.setDescription(description);
        listing.setQuantity(quantity);
        return listingDao.save(listing);
    }

    public void updateListing(Long listingId, Long userId, String title, String description, int quantity) {
        Listing listing = new Listing();
        listing.setId(listingId);
        listing.setUserId(userId);
        listing.setTitle(title);
        listing.setDescription(description);
        listing.setQuantity(quantity);
        listingDao.update(listing);
    }

    public void deleteListing(Long listingId, Long userId) {
        listingDao.delete(listingId, userId);
    }

    public Optional<Listing> findById(Long id) {
        return listingDao.findById(id);
    }

    public List<Listing> findByOwner(Long ownerId) {
        return listingDao.findByOwner(ownerId);
    }

    public List<Listing> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return listingDao.findAll();
        }
        return listingDao.searchByKeyword(keyword);
    }

    public List<Listing> findAll() {
        return listingDao.findAll();
    }
}

