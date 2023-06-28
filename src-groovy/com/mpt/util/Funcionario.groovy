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

/**
 * Permite obtener los nombres completos o nombre de usuario de un funcionario
 * 
 * @author alexjcm
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

		return userName
	}
}