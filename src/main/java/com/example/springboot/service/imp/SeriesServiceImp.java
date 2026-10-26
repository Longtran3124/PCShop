package com.example.springboot.service.imp;

import com.example.springboot.dto.request.series.create.SeriesCreateRequest;
import com.example.springboot.dto.request.series.update.SeriesUpdateRequest;
import com.example.springboot.dto.response.series.SeriesResponse;
import com.example.springboot.entity.Series;
import com.example.springboot.mapper.SeriesMapper;
import com.example.springboot.repository.SeriesRepository;
import com.example.springboot.service.SeriesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeriesServiceImp implements SeriesService {

    SeriesRepository seriesRepository;
    private Series getEntityById(Long id) {
        return seriesRepository.findById(id)   // findById gốc (không lọc deleted vì không có)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy series với id: " + id));
    }

    @Override
    public SeriesResponse getById(Long id) {
        return SeriesMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<SeriesResponse> findAll() {
        return seriesRepository.findAll()
                .stream().map(SeriesMapper::toResponse).toList();
    }

    @Override
    public List<SeriesResponse> findByNameContaining(String keyword) {
        return seriesRepository.findByNameContaining(keyword)
                .stream().map(SeriesMapper::toResponse).toList();
    }

    @Override
    public List<SeriesResponse> findByBrandId(Long brandId) {
        return seriesRepository.findByBrandId(brandId)
                .stream().map(SeriesMapper::toResponse).toList();
    }

    @Override
    public SeriesResponse create(SeriesCreateRequest request) {
        Series s = SeriesMapper.toEntity(request);
        s.setSlug(generateSlug(request.getName()));
        return SeriesMapper.toResponse(seriesRepository.save(s));
    }

    @Override
    public SeriesResponse update(Long id, SeriesUpdateRequest request) {
        Series existing = getEntityById(id);
        if (request.getName() != null)    existing.setName(request.getName());
        if (request.getBrandId() != null) existing.setBrandId(request.getBrandId());
        return SeriesMapper.toResponse(seriesRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Series existing = getEntityById(id);   // kiểm tra tồn tại trước
        seriesRepository.delete(existing);      // XÓA CỨNG khỏi DB
    }

    private String generateSlug(String name) {
        return name.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
