package ru.practicum.shareit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@RequiredArgsConstructor(onConstructor_ = @Autowired)

@SpringBootTest
@AutoConfigureMockMvc
public class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;


    protected static BookingDto bookingDto = new BookingDto();
    protected static BookingDto bookingDto2 = new BookingDto();


    protected static UserDto userDto = UserDto.builder().email("1update@user.com").name("1update").build();
    protected static UserDto userDto2 = UserDto.builder().email("2update@user.com").name("2update").build();
    protected static UserDto userDto3 = UserDto.builder().email("4update@user.com").name("4update").build();

    protected static ItemDto itemDto = ItemDto.builder()
            .name("Дрель")
            .description("Простая дрель")
            .available(true)
            .build();
    protected static ItemDto itemDto2 = ItemDto.builder()
            .name("Дрель2")
            .description("Простая дрель2")
            .available(true)
            .build();

    protected static String objectToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT)
                .build();
        return mapper.writeValueAsString(object);
    }
}