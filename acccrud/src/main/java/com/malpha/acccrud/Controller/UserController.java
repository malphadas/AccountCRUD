package com.malpha.acccrud.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.malpha.acccrud.Model.Usuario;
import com.malpha.acccrud.Repo.UserRepository;

/* funcionalidades CRUD
 * create
 * read
 * update
 * delete
 */

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository UserRepo;

	public UserController(UserRepository userRepo) {
    this.UserRepo = userRepo;
  }
	
	@GetMapping ("/admin")
	public List<Usuario> list(){
		return UserRepo.findAll();		
	}
	
	@PostMapping("/register")
	public Usuario addUser(@RequestParam("username") String username,
						   @RequestParam("name") String name,
                       	   @RequestParam("email") String email,
                           @RequestParam("password") String password) {
		
		Usuario newUser = new Usuario();
    	newUser.setUsername(username);
    	newUser.setName(name);
    	newUser.setEmail(email);
    	newUser.setPassword(password);
		return UserRepo.save(newUser);
	}

	@GetMapping("/{id}")
  public Usuario getUserPage(@PathVariable("id") Long userId) {
    // Retrieve user from the repository based on ID
    Optional<Usuario> optionalUser = UserRepo.findById(userId);
	if (optionalUser.isEmpty()) {
	        return null;
	    }
	Usuario existingUsuario = optionalUser.get();
    
    // Update the visit count
    existingUsuario.setVisitCount(existingUsuario.getVisitCount() + 1);
	UserRepo.save(existingUsuario);
	return existingUsuario;
  }
	
	@PatchMapping("/{id}")
	public ResponseEntity<Usuario> changeInfo(@PathVariable("id")Long userId,@RequestBody Usuario updatedUsuario) {
		Optional<Usuario> optionalUser = UserRepo.findById(userId);
	    if (optionalUser.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    Usuario existingUsuario = optionalUser.get();

        if (updatedUsuario.getUsername() != null) {
	        existingUsuario.setUsername(updatedUsuario.getUsername());
	    }
	    
	    if (updatedUsuario.getPassword() != null) {
	        existingUsuario.setPassword(updatedUsuario.getPassword());
	    }
	    

	    UserRepo.save(existingUsuario);

	    return ResponseEntity.ok(existingUsuario);	
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Usuario> deleteUser(@PathVariable("id") Long id){
	    Optional<Usuario> optionalUser = UserRepo.findById(id);
	    
		if (optionalUser.isPresent()) {
	        UserRepo.delete(optionalUser.get());
	        return ResponseEntity.ok().build(); // Successful delete, return 200 OK
	    } else {
	        return ResponseEntity.notFound().build(); // User with the specified ID not found, return 404 Not Found
	    }
		
	}
	
	
	
}