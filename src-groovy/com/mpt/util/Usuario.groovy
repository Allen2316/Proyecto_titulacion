package com.mpt.util

import java.util.logging.Logger
import java.security.SecureRandom

import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.ProfileAPI
import org.bonitasoft.engine.identity.ContactDataCreator
import org.bonitasoft.engine.identity.ContactDataUpdater
import org.bonitasoft.engine.identity.Group
import org.bonitasoft.engine.identity.GroupSearchDescriptor
import org.bonitasoft.engine.identity.Role
import org.bonitasoft.engine.identity.RoleSearchDescriptor
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserCreator;
import org.bonitasoft.engine.identity.UserSearchDescriptor
import org.bonitasoft.engine.identity.UserUpdater
import org.bonitasoft.engine.profile.Profile
import org.bonitasoft.engine.profile.ProfileMember
import org.bonitasoft.engine.profile.ProfileSearchDescriptor
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.impl.SearchResultImpl

import org.bonitasoft.engine.profile.ProfileCriterion

import com.mpt.constantes.MPTConstants

/**
 * Permite crear o actualizar usuarios
 * 
 * @author ajcm
 * @version 1.0
 */
class Usuario {

	private static final Logger logger = Logger.getLogger(Usuario.class.toString())

	/**
	 * Permite obtener el id de un grupo de la organización mediante su nombre, por ejemplo: estudiante
	 * 
	 * @param identityAPI, el actual IdentityAPI
	 * @param groupName
	 * @return Long id, the Group identified by its id (-1 if not match any results)
	 *
	 */
	static Long getGroupId(IdentityAPI identityAPI, String groupName) {
		if (groupName.isEmpty()) {
			throw new Exception("Se debe enviar el groupName")
		}

		Long id = -1L;

		SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 100);
		builder.filter(GroupSearchDescriptor.NAME, groupName);
		SearchResultImpl<Group> groupResults = identityAPI.searchGroups(builder.done());
		
		for (group in groupResults.getResult()){
			logger.info("----> getGroupId: " + group.id)
			return group.id;
		}

		return id;
	}

	/**
	 * Permite obtener el id de un perfil de Bonita mediante su nombre, por ejemplo: User
	 * 
	 * @param profileAPI, el actual ProfileAPI
	 * @param profileName
	 * @return Long id, the Profile identified by its id (-1 if not match any results)
	 */
	static Long getProfileId(ProfileAPI profileAPI, String profileName) {
		if (profileName.isEmpty()) {
			throw new Exception("Se debe enviar el profileName")
		}

		Long id = -1L;

		SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 100);
		builder.filter(ProfileSearchDescriptor.NAME, profileName);
		SearchResultImpl<Profile> profileResults = profileAPI.searchProfiles(builder.done())

		for (profile in profileResults.getResult()){
			logger.info("----> getProfileId: " + profile.id)
			return profile.id;
		}

		return id;
	}

	/**
	 * Permite obtener el id de un rol mediante su nombre, por ejemplo: member
	 * 
	 * @param identityAPI, el actual IdentityAPI
	 * @param roleName, name of the role from which to obtain the id
	 * @return Long id, the Role identified by its id (-1 if not match any results)
	 */
	static Long getRoleId(IdentityAPI identityAPI, String roleName) {
		if (roleName.isEmpty()) {
			throw new Exception("Se debe enviar el roleName")
		}

		Long id = -1L;

		SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 100);
		builder.filter(RoleSearchDescriptor.NAME, roleName);
		SearchResultImpl<Role> roleResults = identityAPI.searchRoles(builder.done())

		for (role in roleResults.getResult()){
			logger.info("----> getRoleId: " + role.id)
			return role.id;
		}

		return id;
	}

	/**
	 * Permite consultar si un usuario existe mediante el username
	 * 
	 * @param identityAPI, el actual IdentityAPI
	 * @param username
	 * @return Boolean, true if user exists otherwise false
	 */
	static Boolean userExists(IdentityAPI identityAPI, String username) {
		if (username.isEmpty()) {
			throw new Exception("Se debe enviar el username")
		}

		SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 1000);
		builder.filter(UserSearchDescriptor.USER_NAME, username);
		SearchResultImpl<User> userResults = identityAPI.searchUsers(builder.done())

		for (user in userResults.getResult()){
			if (user.getUserName() == username) {
				logger.info("----> El usuario: " + username + " ya existe")
				return true;
			}
		}

		logger.info("----> El usuario: " + username + " aún no existe")
		return false;
	}

	/**
	 * Permite obtener un usuario mediante el username en caso de que exista
	 * 
	 * @param identityAPI, el actual IdentityAPI
	 * @param username
	 * @return User, the user if exists or null if doesn't exists
	 */
	static User findUserByUsername(IdentityAPI identityAPI, String username) {
		if (username.isEmpty()) {
			throw new Exception("Se debe enviar el username")
		}

		SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 1000);
		builder.filter(UserSearchDescriptor.USER_NAME, username);
		SearchResultImpl<User> userResults = identityAPI.searchUsers(builder.done())

		for (user in userResults.getResult()){
			if (user.getUserName() == username) {
				logger.info("----> findUserByUsername: " + user)
				return user;
			}
		}

		return null;
	}

	/**
	 * Permite crear o actualizar una cuenta para un usuario en Bonita Portal
	 * 
	 * @param apiAccessor, the current APIAccessor
	 * @param username, username for the new account
	 * @param password, password for the new account
	 * @param name, name for the contact data
	 * @param userProfileId, id of the profile to assign to the account
	 * @param groupId, id of the group to assign to the account
	 * @param roleId, id of the role to assign to the account
	 * @return User, new user created or old user with existing username
	 */
	static User createAccount(APIAccessor apiAccessor, String username, String password, def mapStudent, Long userProfileId, Long groupId, Long roleId) {
		if (username.isEmpty()) {
			throw new Exception("Se debe enviar el username")
		}

		if (password.isEmpty()) {
			throw new Exception("Se debe enviar el password")
		}

		if (userProfileId == null) {
			throw new Exception("Se debe enviar el userProfileId")
		}

		if (groupId == null) {
			throw new Exception("Se debe enviar el groupId")
		}

		if (roleId == null) {
			throw new Exception("Se debe enviar el roleId")
		}
		
		ContactDataCreator proContactDataCreator = new ContactDataCreator()
		proContactDataCreator.setEmail(mapStudent?.email)

		UserCreator userCreator = new UserCreator(username, password);
		userCreator.setFirstName(mapStudent?.firstName);
		userCreator.setLastName(mapStudent?.lastName);
		userCreator.setProfessionalContactData(proContactDataCreator)

		long definitionId = apiAccessor.identityAPI.getCustomUserInfoDefinitions(0, 1)
				.find { it.name == MPTConstants.IDENTIFICATION_DOCUMENT_TYPE }?.id

		User user = null

		try {
			
			// STEP 1.- Create new user and set cedula
			user = apiAccessor.identityAPI.createUser(userCreator);
			apiAccessor.identityAPI.setCustomUserInfoValue(definitionId, user.getId(), mapStudent?.cedula)
			//logger.info("----> STEP 1.- Se ha creado el usuario y establecido su cédula")

		} catch (Exception ex){
			// Find existing user by username
			user = apiAccessor.identityAPI.getUserByUserName(username);
			// Set or update cedula
			apiAccessor.identityAPI.setCustomUserInfoValue(definitionId, user.getId(), mapStudent?.cedula)
			logger.warning("----> Warning - STEP 1.1.- Set or update cedula: " + ex.getMessage())
			
			// Set or update firstName, lastName and professional email
			ContactDataUpdater proContactDataUpdater = new ContactDataUpdater()
			proContactDataUpdater.setEmail(mapStudent?.email)
			UserUpdater userUpdater = new UserUpdater()
			userUpdater.setFirstName(mapStudent?.firstName)
			userUpdater.setLastName(mapStudent?.lastName)
			userUpdater.setEnabled(true)
			userUpdater.setProfessionalContactData(proContactDataUpdater)
			
			apiAccessor.identityAPI.updateUser(user.getId(), userUpdater)
			//logger.warning("----> Warning - STEP 1.2.- Set or update firstName, lastName and professional email: " + ex.getMessage())
		}


		try {
			// STEP 2.- Add the user to a Bonita profile (Definir un perfil para el usuario)
			apiAccessor.profileAPI.createProfileMember(userProfileId, user.getId(), groupId, roleId);
			//logger.info("----> STEP 2.- Se ha creado un miembro del perfil")

		} catch (Exception ex ) {
			//logger.warning("----> Warning - STEP 2.- El miembro del perfil ya existe: " + ex.getMessage())
		}


		try {
			// STEP 3.- Add a user to a group
			apiAccessor.identityAPI.addUserMembership(user.getId(), groupId, roleId);
			//logger.info("----> - STEP 3.- Se asoció el usuario con el grupo y rol. (Dicha asociación se denomina membresia de usuario)")
		} catch (Exception ex) {
			//logger.warning("----> Warning - STEP 3.- El usuario ya pertenece a dicho grupo y ya tiene dicho rol: " + ex.getMessage())
		}

		return user

	}

	/**
	 * Permite generar un password
	 * 
	 * @param length, the length of the password to return
	 * @return String, random password
	 */
	static String generateRandomString(int length) {
		def chars = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
		def random = new SecureRandom()
		def result = new StringBuilder(length)
		for (int i = 0; i < length; i++) {
			result.append(chars[random.nextInt(chars.length())])
		}
		
		return result.toString()
	}
}
