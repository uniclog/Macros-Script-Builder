package local.uniclog.services;

import local.uniclog.ui.controlls.model.MacrosItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static local.uniclog.utils.ConfigConstants.TEMPLATE_CONFIG_PATH;
import static org.junit.jupiter.api.Assertions.*;

class DataConfigServiceTest {
    private final MacrosItem item = new MacrosItem("item", "content", TEMPLATE_CONFIG_PATH);
    private DataConfigService service;

    @BeforeEach
    void setUp() {
        service = new DataConfigService();
    }

    @AfterEach
    void tearDown() {
        assertTrue(new File(TEMPLATE_CONFIG_PATH).delete());
    }

    @Test
    void initializeConfigTest() {
        assertNotNull(service);
        assertNotNull(service.getItems());
    }

    @Test
    void addNewItemTest() {
        service.addItem(item);
        assertEquals(List.of(item), service.getItems());
        assertEquals(List.of(item), new DataConfigService().getItems());
    }

    @Test
    void addItemsTest() {
        var item2 = new MacrosItem("item_2", "content", TEMPLATE_CONFIG_PATH);
        service.addItem(item);
        service.addItem(item2);
        assertEquals(List.of(item, item2), service.getItems());
        assertEquals(List.of(item, item2), new DataConfigService().getItems());
    }

    @Test
    void getItemsTest() {
        service.addItem(item);
        assertEquals(List.of(item), service.getItems());
    }

    @Test
    void getRemoveItemsTest() {
        service.addItem(item);
        assertEquals(List.of(item), new DataConfigService().getItems());
        service.removeItem(item);
        assertTrue(new DataConfigService().getItems().isEmpty());
    }

    @Test
    void modifyItemByIndexText() {
        service.addItem(item);
        assertEquals(List.of(item), new DataConfigService().getItems());
        item.setText("newText");
        service.modifyItemByIndex(0, item);
        var actual = service.getItems().get(0).getText();
        assertEquals("newText", actual);
        assertEquals(1, service.getItems().size());
    }
}