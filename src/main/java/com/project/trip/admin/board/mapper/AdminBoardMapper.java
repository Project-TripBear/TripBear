package com.project.trip.admin.board.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.project.trip.admin.board.model.IntegratedBoardDTO;

@Mapper
public interface AdminBoardMapper {

    int getTotalBoardCount(@Param("boardType") String boardType);

    List<IntegratedBoardDTO> getIntegratedBoardList(Map<String, Object> paramMap);
}
