package com.example.idlegoodsinfo.web;

import com.example.idlegoodsinfo.entity.Listing;
import com.example.idlegoodsinfo.entity.User;
import com.example.idlegoodsinfo.service.ListingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ListingServlet", urlPatterns = {"/listings", "/listings/new", "/listings/edit", "/listings/delete"})
public class ListingServlet extends HttpServlet {
    private ListingService listingService;

    @Override
    public void init() {
        this.listingService = new ListingService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = resolvePath(req);
        User currentUser = requireLogin(req, resp);
        if (currentUser == null) {
            return;
        }
        switch (path) {
            case "/listings" -> showListings(req, resp);
            case "/listings/new" -> req.getRequestDispatcher("/listing-form.jsp").forward(req, resp);
            case "/listings/edit" -> showEditForm(req, resp, currentUser);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = resolvePath(req);
        User currentUser = requireLogin(req, resp);
        if (currentUser == null) {
            return;
        }
        switch (path) {
            case "/listings/new" -> handleCreate(req, resp, currentUser);
            case "/listings/edit" -> handleUpdate(req, resp, currentUser);
            case "/listings/delete" -> handleDelete(req, resp, currentUser);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showListings(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<Listing> listings = listingService.search(keyword);
        req.setAttribute("listings", listings);
        req.getRequestDispatcher("/listings.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws ServletException, IOException {
        Long id = parseId(req);
        if (id == null) {
            resp.sendRedirect(req.getContextPath() + "/listings");
            return;
        }
        Optional<Listing> listingOpt = listingService.findById(id);
        if (listingOpt.isEmpty() || !listingOpt.get().getUserId().equals(currentUser.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        req.setAttribute("listing", listingOpt.get());
        req.getRequestDispatcher("/listing-form.jsp").forward(req, resp);
    }

    private void handleCreate(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        listingService.createListing(currentUser.getId(), title, description, quantity);
        resp.sendRedirect(req.getContextPath() + "/listings");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException {
        Long id = parseId(req);
        if (id == null) {
            resp.sendRedirect(req.getContextPath() + "/listings");
            return;
        }
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        listingService.updateListing(id, currentUser.getId(), title, description, quantity);
        resp.sendRedirect(req.getContextPath() + "/listings");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException {
        Long id = parseId(req);
        if (id != null) {
            listingService.deleteListing(id, currentUser.getId());
        }
        resp.sendRedirect(req.getContextPath() + "/listings");
    }

    private Long parseId(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            return null;
        }
        try {
            return Long.valueOf(idStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private User requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
        return currentUser;
    }

    private String resolvePath(HttpServletRequest req) {
        String path = req.getServletPath();
        String extra = req.getPathInfo();
        return (extra == null) ? path : path + extra;
    }
}
