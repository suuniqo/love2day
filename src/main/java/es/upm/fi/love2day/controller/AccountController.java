package es.upm.fi.love2day.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {
    ProductoService productoService;
    ProductoMapper productoMapper;

    public ProductoController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    // Todos los productos
    @GetMapping("/productos/todos")
    List<ProductoDto> obtenerTodosProductos() {
        List<Producto> productos = productoService.obtenerTodosProductos();
        return productoMapper.toDtoList(productos);
    }

    @GetMapping("/productos/todosPageable")
    // Ejemplo: GET /productos/todosPageable?page=0&size=5&sort=nombre,asc
    // Ejemplo: GET /productos/todosPageable?nombre=leche&page=0&size=5&sort=nombre,asc
    public Page<ProductoDto> obtenerTodosProductos(
        @RequestParam(required = false) String nombre,
        @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        Page<Producto> productos = productoService.obtenerProductos(nombre, pageable);
        return productos.map(productoMapper::toDto);
    }

    @GetMapping("/producto/{id}")
    ProductoDto obtenerProducto(@PathVariable long id) throws ResponseStatusException{
        Producto producto = productoService
            .obtenerProducto(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No existe un producto con id " + id
            ));

        return productoMapper.toDto(producto);
    }

    @PostMapping("/producto/anadir")
    @ResponseStatus(HttpStatus.CREATED)
    void anadirProducto(@RequestBody Producto producto) throws ResponseStatusException {
        productoService.anadirProducto(producto);
    }
}
