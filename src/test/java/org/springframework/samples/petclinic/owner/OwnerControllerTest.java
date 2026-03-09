package org.springframework.samples.petclinic.owner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class OwnerControllerTest {

	private MockMvc mockMvc;

	private OwnerRepository owners;

	@BeforeEach
	void setUp() {
		this.owners = Mockito.mock(OwnerRepository.class);
		OwnerController controller = new OwnerController(this.owners);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(get("/owners/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(get("/owners/find"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/findOwners"));
	}

	@Test
	void testProcessFindForm_withNoResults_staysOnFindOwners() throws Exception {
		Page<Owner> empty = Page.empty();
		when(this.owners.findByLastNameStartingWith(eq("D"), any(Pageable.class))).thenReturn(empty);

		this.mockMvc.perform(get("/owners").param("lastName", "D"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/findOwners"));
	}

	@Test
	void testProcessFindForm_withOneResult_redirectsToOwner() throws Exception {
		Owner owner = new Owner();
		owner.setId(1);
		owner.setLastName("D");
		Page<Owner> one = new PageImpl<>(List.of(owner));

		when(this.owners.findByLastNameStartingWith(eq("D"), any(Pageable.class))).thenReturn(one);

		this.mockMvc.perform(get("/owners").param("lastName", "D"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/1"));
	}

	@Test
	void testProcessFindForm_withMultipleResults_showsOwnersList() throws Exception {
		Owner o1 = new Owner();
		o1.setId(1);
		o1.setLastName("D");

		Owner o2 = new Owner();
		o2.setId(2);
		o2.setLastName("D");

		Page<Owner> many = new PageImpl<>(List.of(o1, o2));
		when(this.owners.findByLastNameStartingWith(eq("D"), any(Pageable.class))).thenReturn(many);

		this.mockMvc.perform(get("/owners").param("lastName", "D"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/ownersList"));
	}

}