package com.malpha.acccrud.Controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import jakarta.annotation.PostConstruct;

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
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepo;

	public UserController(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@GetMapping("/admin")
	public List<Usuario> list() {
		return userRepo.findAll();
	}

	@PostMapping("/register")
	public RedirectView addUser(@RequestParam("username") String username,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password) {

		boolean emailExists = userRepo.existsByEmail(email);
		boolean usernameExists = userRepo.existsByUsername(username);

		if (emailExists) {
			// Email already registered, return a popup message
			String errorMessage = "Email already registered";

			// Pass the error message as a query parameter in the redirect URL
			String redirectUrl = "http://localhost:8080/register?error="
					+ URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

			// Create a RedirectView with the redirect URL
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl(redirectUrl);

			return redirectView;
		} else if (usernameExists) {
			// Username already in use, return a popup message
			String errorMessage = "Username already in use";

			// Pass the error message as a query parameter in the redirect URL
			String redirectUrl = "http://localhost:8080/register?error="
					+ URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

			// Create a RedirectView with the redirect URL
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl(redirectUrl);

			return redirectView;
		}

		Usuario newUser = new Usuario();
		newUser.setUsername(username);
		newUser.setName(name);
		newUser.setEmail(email);
		newUser.setPassword(passwordEncoder.encode(password));
		newUser.setRole("ROLE_USER");
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
	public ResponseEntity<Usuario> changeInfo(@PathVariable("id") Long userId, @RequestBody Usuario updatedUsuario) {
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

			BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
			if (passEncoder.matches(user.getPassword(), password)) {
				userRepo.delete(user);
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.badRequest().build(); // Incorrect password
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostConstruct
	public void createAdminUser() {
		// Create an in-memory user with ADMIN role (for demo purposes)
		Usuario adminUser = new Usuario();
		adminUser.setName("admin");
		adminUser.setEmail("adminUser@exemple.com");
		adminUser.setPassword(passwordEncoder.encode("admin"));
		adminUser.setUsername("admin");
		adminUser.setRole("ROLE_ADMIN");

		// Add the admin user to the repository
		userRepo.save(adminUser);
	}

@GetMapping("/s")
public RedirectView success(Authentication authentication) {
	String redirectUrl;

    // Retrieve the Usuario object from the repository using the username
    String username = authentication.getName();

	Usuario usuario = userRepo.findByUsername(username);
	
    if (usuario == null) {
        // Handle the case where the user is not found in the repository
        throw new IllegalStateException("User not found");
    }

    if (username.equals("admin")) {
        redirectUrl = "http://localhost:8080/admin";

    } else {
        // Retrieve the userId associated with the authenticated user
		Long userId = usuario.getId();

        redirectUrl = "http://localhost:8080/" + userId;
    }

	RedirectView redirectView = new RedirectView();
		redirectView.setUrl(redirectUrl);

		return redirectView;

}

}