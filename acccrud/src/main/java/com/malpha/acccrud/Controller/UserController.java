package com.malpha.acccrud.Controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
import org.springframework.web.servlet.view.RedirectView;

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
	private UserRepository userRepo;

	public UserController(UserRepository userRepo) {
    this.userRepo = userRepo;
  }
	
	@GetMapping ("/admin")
	public List<Usuario> list(){
		return userRepo.findAll();		
	}
	
	@PostMapping("/register")
	public RedirectView addUser(@RequestParam("username") String username,
						   @RequestParam("name") String name,
                       	   @RequestParam("email") String email,
                           @RequestParam("password") String password) {

	boolean emailExists = userRepo.existsByEmail(email);

  if (emailExists) {
    // Email already registered, return a popup message
    String errorMessage = "Email already registered";

    // Pass the error message as a query parameter in the redirect URL
    String redirectUrl = "http://localhost:8080/register?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

    // Create a RedirectView with the redirect URL
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl(redirectUrl);

    return redirectView;
  }
		
		Usuario newUser = new Usuario();
    	newUser.setUsername(username);
    	newUser.setName(name);
    	newUser.setEmail(email);
    	newUser.setPassword(password);
		userRepo.save(newUser);
		long userId = newUser.getId();

    	String redirectUrl = "http://localhost:8080/" + userId;

    	RedirectView redirectView = new RedirectView();
    	redirectView.setUrl(redirectUrl);

    	return redirectView;
	}

	@GetMapping("/{id}")
  public Usuario getUserPage(@PathVariable("id") Long userId) {
    // Retrieve user from the repository based on ID
    Optional<Usuario> optionalUser = userRepo.findById(userId);
	if (optionalUser.isEmpty()) {
	        return null;
	    }
	Usuario existingUsuario = optionalUser.get();
    
    // Update the visit count
    existingUsuario.setVisitCount(existingUsuario.getVisitCount() + 1);
	userRepo.save(existingUsuario);
	return existingUsuario;
  }
	
	@PatchMapping("/{id}")
	public ResponseEntity<Usuario> changeInfo(@PathVariable("id") Long userId,@RequestBody Usuario updatedUsuario) {
		Optional<Usuario> optionalUser = userRepo.findById(userId);
	    if (optionalUser.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    Usuario existingUsuario = optionalUser.get();
	    
	    existingUsuario.setUsername(updatedUsuario.getUsername());
        existingUsuario.setPassword(updatedUsuario.getPassword());

	    userRepo.save(existingUsuario);

	    return ResponseEntity.ok(existingUsuario);	
	}

	
	@DeleteMapping("/{id}")
public ResponseEntity<Usuario> deleteUser(@PathVariable("id") Long id, @RequestParam("password") String password) {
    Optional<Usuario> optionalUser = userRepo.findById(id);

    if (optionalUser.isPresent()) {
        Usuario user = optionalUser.get();

        if (user.getPassword().equals(password)) {
            userRepo.delete(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build(); // Incorrect password
        }
    } else {
        return ResponseEntity.notFound().build();
    }
}	
}