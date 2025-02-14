package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.dto.SharedCollectionResponseDto;
import com.isima.gestionbibliotheque.model.AccessKey;
import com.isima.gestionbibliotheque.model.Collection;
import com.isima.gestionbibliotheque.model.Permission;
import com.isima.gestionbibliotheque.repository.AccessKeyRepository;
import com.isima.gestionbibliotheque.repository.CollectionRepository;
import com.isima.gestionbibliotheque.service.AccessKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccessKeyServiceImpl implements AccessKeyService {

    private final AccessKeyRepository accessKeyRepository;
    private final CollectionRepository collectionRepository;

    @Autowired
    public AccessKeyServiceImpl(
            AccessKeyRepository accessKeyRepository,
            CollectionRepository collectionRepository
    ) {
        this.accessKeyRepository = accessKeyRepository;
        this.collectionRepository = collectionRepository;
    }
    @Override
    public AccessKey getAccessKeyById(UUID id) {
        return accessKeyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Invalid access key"));
    }

    @Override
    public SharedCollectionResponseDto createAccessKey(Long collectionId, Set<String> permissions, String serverUri) {
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find collection with Id %d", collectionId))
        );
        AccessKey accessKey = new AccessKey();
        accessKey.setCollection(collection);
        accessKey.setPermissions(permissions.stream().map(Permission::valueOf).collect(Collectors.toSet()));
        long EXPIRATION_TIME = 24*60*60*60;
        accessKey.setExpiredAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        accessKeyRepository.save(accessKey);
        log.info("Collection Id: "+accessKey.getId());
        return SharedCollectionResponseDto.builder()
                .url(String.format("%s/api/collections/%s?key=%s", serverUri,collection.getId(),accessKey.getId()))
                .build();
    }


}
