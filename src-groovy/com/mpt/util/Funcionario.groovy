package com.mpt.util

import java.util.logging.Logger

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.exception.SearchException
import org.bonitasoft.engine.identity.GroupSearchDescriptor
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserNotFoundException
import org.bonitasoft.engine.identity.UserSearchDescriptor
import org.bonitasoft.engine.search.SearchOptionsBuilder
import com.mpt.constantes.MPTConstants

/**
 * Permite obtener los nombres completos o nombre de usuario de un funcionario
 * 
 * @author Allen
 * @version 1.0
 */
class Funcionario {

	private static final Logger logger = Logger.getLogger(Funcionario.class.toString())

	/**
	 * Obtiene los nombres completos del funcionario del grupo enviado como parámetro.
	 * 
	 * @param groupName
	 * @param identityAPI
	 * @return Nombres completos del funcionario
	 */
	static String getFullName(String groupName, IdentityAPI identityAPI) {
		if (!groupName) {
			throw new Exception("Se debe enviar el nombre del grupo del funcionario")
		}
		
		String fullName = ""
		try {
			def builderGoup = new SearchOptionsBuilder(0, 1)
			builderGoup.filter(GroupSearchDescriptor.NAME, groupName)
			Long groupId = identityAPI.searchGroups(builderGoup.done()).getResult().get(0).getId()

			def builderUser = new SearchOptionsBuilder(0, 1);
			builderUser.filter(UserSearchDescriptor.GROUP_ID, groupId);
			User user = identityAPI.searchUsers(builderUser.done()).getResult().get(0)
			fullName = user.firstName + " " + user.lastName
		} catch (SearchException e) {
			logger.severe("Ha ocurrido una excepción durante la búsqueda del usuario en la organización: " + e.getMessage())
		}

		return fullName.trim()
	}
	
	/**
	 * Obtiene los nombres completos del funcionario del grupo enviado como parámetro.
	 *
	 * @param userName
	 * @param identityAPI
	 * @return Nombres completos del funcionario
	 */
	static String getFullNameWithUserName(String userName, IdentityAPI identityAPI) {
		
			
		String fullName = ""
		try{			
			Long id = identityAPI.getUserByUserName(userName).getId()
						
			fullName = identityAPI.getUser(id).getFirstName() + " " + identityAPI.getUser(id).getLastName()
			
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado: " + e.getMessage())
		}

		return fullName.trim()
	}

	/**
	 * Obtiene los nombres completos del funcionario utilizando el processInstanceId.
	 * 
	 * @param processAPI
	 * @param processInstanceId
	 * @param identityAPI
	 * @return
	 */
	static String getUsername(ProcessAPI processAPI, Long processInstanceId, IdentityAPI identityAPI) {
		if (!processInstanceId) {
			throw new Exception("Se debe enviar el processInstanceId del funcionario")
		}

		String userName = ""
		try{
			long userId = processAPI.getProcessInstance(processInstanceId).getStartedBy()
			userName = identityAPI.getUser(userId).getUserName()
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado en la organización: " + e.getMessage())
		}

		return userName.trim()
	}
	
	/**
	 * Obtiene la cédula del funcionario de acuerdo al username solicitante
	 *
	 * @param groupName 
	 * @param identityAPI 
	 * @return String
	 */
	static String getCedulaDocent(String groupName, IdentityAPI identityAPI) {
		if (!groupName) {
			throw new Exception("Se debe enviar el nombre del grupo del funcionario")
		}
		
		String cedula = ""
		try {
			def builderGoup = new SearchOptionsBuilder(0, 1)
			builderGoup.filter(GroupSearchDescriptor.NAME, groupName)
			Long groupId = identityAPI.searchGroups(builderGoup.done()).getResult().get(0).getId()

			def builderUser = new SearchOptionsBuilder(0, 1);
			builderUser.filter(UserSearchDescriptor.GROUP_ID, groupId);
			User user = identityAPI.searchUsers(builderUser.done()).getResult().get(0)
			
			
			cedula = identityAPI.getCustomUserInfo(user.getId(), 0, 1)
			.find { MPTConstants.IDENTIFICATION_DOCUMENT_TYPE == it.getDefinition().getName() }
			?.getValue();
			
			
		} catch (SearchException e) {
			logger.severe("Ha ocurrido una excepción durante la búsqueda del usuario en la organización: " + e.getMessage())
		}

		logger.info("=======================cedula================= : "+ cedula)
		return cedula.trim()
	}
	
	/**
	 * Obtiene el ID del funcionario de acuerdo al nombre del grupo solicitante
	 *
	 * @param groupName
	 * @param identityAPI
	 * @return Long
	 */
	static Long getId(String groupName, IdentityAPI identityAPI) {
		if (!groupName) {
			throw new Exception("Se debe enviar el nombre del grupo del funcionario")
		}
		
		Long userId = -1L;
		//Long userId = ""
		try {
			def builderGoup = new SearchOptionsBuilder(0, 1)
			builderGoup.filter(GroupSearchDescriptor.NAME, groupName)
			Long groupId = identityAPI.searchGroups(builderGoup.done()).getResult().get(0).getId()

			def builderUser = new SearchOptionsBuilder(0, 1);
			builderUser.filter(UserSearchDescriptor.GROUP_ID, groupId);
			User user = identityAPI.searchUsers(builderUser.done()).getResult().get(0)
			
			
			userId = user.getId()
			
			
		} catch (SearchException e) {
			logger.severe("Ha ocurrido una excepción durante la búsqueda del usuario en la organización: " + e.getMessage())
		}

		return userId
	}
	
	/**
	 * Obtiene los nombres completos del funcionario del grupo enviado como parámetro.
	 *
	 * @param userName
	 * @param identityAPI
	 * @return Nombres completos del funcionario
	 */
	static Long getIdByUserName(String userName, IdentityAPI identityAPI) {
		
			
		Long userId = -1L;
		try{
			userId = identityAPI.getUserByUserName(userName).getId()									
			
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado: " + e.getMessage())
		}

		return userId
	}
	
	/**
	 * Retorna el Email del usurio con el ID pasado por parametro
	 * @param userId
	 * @param identityAPI
	 * @return El Email en String
	 */
	
	static String getEmailById(Long userId, IdentityAPI identityAPI) {
		String email = "";
		try{			
			email = identityAPI.getUserContactData(userId,false).getEmail();
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado: " + e.getMessage())
		}

		return email;
	}

}