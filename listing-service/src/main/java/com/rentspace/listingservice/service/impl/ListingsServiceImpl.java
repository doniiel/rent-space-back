package com.rentspace.listingservice.service.impl;

import com.rentspace.listingservice.dto.ListingCreateRequest;
import com.rentspace.listingservice.dto.ListingDto;
import com.rentspace.listingservice.dto.ListingUpdateRequest;
import com.rentspace.listingservice.entity.Listing;
import com.rentspace.listingservice.exception.ListingNotFoundException;
import com.rentspace.listingservice.mapper.ListingMapper;
import com.rentspace.listingservice.repository.ListingsRepository;
import com.rentspace.listingservice.service.ListingPhotoService;
import com.rentspace.listingservice.service.ListingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingsServiceImpl implements ListingsService {

    private final ListingsRepository listingsRepository;
    private final ListingMapper listingMapper;
    private final ListingPhotoService listingPhotoService;

    @Override
    @Transactional
    public ListingDto createListing(ListingCreateRequest request) {
        Listing listing = listingMapper.toEntity(request);
        listingsRepository.save(listing);

        log.info("Создано объявление с ID: {}", listing.getId());
        return listingMapper.toDto(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public ListingDto getListingById(Long id) {
        Listing listing = listingsRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", id));

        return listingMapper.toDto(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListingDto> getAllListings() {
        return listingsRepository.findAll().stream()
                .map(listingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListingDto> getAllUserListings(Long userId) {
        return listingsRepository.findByUserId(userId).stream()
                .map(listingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ListingDto updateListing(Long listingId, ListingUpdateRequest request) {
        Listing listing = listingsRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));

        listingMapper.updateListingFromRequest(request, listing);
        listingsRepository.save(listing);

        return listingMapper.toDto(listing);
    }

    @Override
    @Transactional
    public void deleteListing(Long listingId) {
        Listing listing = listingsRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Listing", "listingId", listingId));

        // Удаление фото перед удалением объявления
        listingPhotoService.deletePhotos(listing.getPhotos());

        listingsRepository.delete(listing);
        log.info("Удалено объявление с ID: {}", listingId);
    }
}
