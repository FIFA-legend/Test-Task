package by.bsuir.controllers;

import by.bsuir.entity.Item;
import by.bsuir.entity.Tag;
import by.bsuir.service.ItemService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@ApiOperation(value = "/item", tags = "Item Controller")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/tags")
    @ApiOperation(value = "Fetch All Tags", response = Tag[].class)
    public Tag[] tags() {
        return Tag.values();
    }

    @GetMapping("/item")
    @ApiOperation(value = "Fetch All Items", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class)
    })
    public Set<Item> getItems() {
        return itemService.getAllItems();
    }

    @PostMapping("/items/selected")
    @ApiOperation(value = "Fetch All by Name and Description and Tags", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class)
    })
    public Set<Item> getItemsByExample(@RequestBody Item example) {
        return itemService.getAllByNameAndDescriptionAndTags(example);
    }

    @GetMapping("/item/{id}")
    @ApiOperation(value = "Fetch Item by Id", response = Item.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class)
    })
    public Item getItem(@PathVariable Long id) {
        return itemService.getItem(id);
    }

    @PostMapping("/item")
    @ApiOperation(value = "Insert Item Record", response = Item.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class)
    })
    public Item saveItem(@RequestBody Item item) {
        return itemService.save(item);
    }

    @PutMapping("/item/{id}")
    @ApiOperation(value = "Update Item Details if is not in user's cart", response = Item.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class)
    })
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        return itemService.update(id, item);
    }

    @PutMapping("/item/force/{id}")
    @ApiOperation(value = "Update Item Details", response = Item.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Item.class)
    })
    public Item forceUpdateItem(@PathVariable Long id, @RequestBody Item item) {
        return itemService.forceUpdate(id, item);
    }

    @DeleteMapping("/item/{id}")
    @ApiOperation(value = "Delete an Item", response = boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = boolean.class)
    })
    public boolean deleteItem(@PathVariable Long id) {
        return itemService.delete(id);
    }

}
