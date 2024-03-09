package com.openclassrooms.starterjwt.controllers.unit;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TeacherControllerUnitTest {
    private static final Long TeacherID1 = 1L;
    private static final Long TeacherID2 = 2L;
    private static final String FirstName = "Stéphane";
    private static final String LastName = "Gmt";
    private static final String NotANumberId = "Not A Number";
    @InjectMocks
    private TeacherController teacherController;
    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;

    @Test
    void testFindTeacherById_shouldReturnResponseOk() {
        // given

        Teacher mockTeacher = Teacher.builder()
                .id(TeacherID1)
                .firstName(FirstName)
                .lastName(LastName)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TeacherDto mockUserDto = new TeacherDto();
        mockUserDto.setId(TeacherID1);
        mockUserDto.setFirstName(FirstName);

        when(teacherService.findById(TeacherID1)).thenReturn(mockTeacher);
        when(teacherMapper.toDto(mockTeacher)).thenReturn(mockUserDto);

        // Act
        ResponseEntity<?> responseEntity = teacherController.findById(TeacherID1.toString());

        // then
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockTeacher.getFirstName(), mockUserDto.getFirstName());
    }

    @Test
    void testFindTeacherById_findById_shouldReturnResponseNotFoundCode() {
        // given

        when(teacherService.findById(TeacherID2)).thenReturn(null);

        // Act
        ResponseEntity<?> responseEntity = teacherController.findById(TeacherID2.toString());

        // then
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    void testTeacherFindById_InvalidId_shouldReturnBadRequest() {
        // Act
        ResponseEntity<?> response = teacherController.findById(NotANumberId);

        // then
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testFindGetAllTeachers_shouldReturnResponseOk() {
        // given
        Teacher mockTeacher = Teacher.builder()
                .id(TeacherID1)
                .firstName(FirstName)
                .lastName(LastName)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Teacher mockTeacher2 = Teacher.builder()
                .id(TeacherID2)
                .firstName("Nath")
                .lastName("Tay")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(mockTeacher);
        teachers.add(mockTeacher2);
        when(teacherService.findAll()).thenReturn(teachers);

        // Act
        ResponseEntity<?> response = teacherController.findAll();

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, (response.getBody()).toString().length());

    }

}
