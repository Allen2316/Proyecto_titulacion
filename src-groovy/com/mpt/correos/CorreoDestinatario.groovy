package com.mpt.correos

import java.util.logging.Logger

import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.identity.GroupNotFoundException
import org.bonitasoft.engine.identity.UserNotFoundException
import org.bonitasoft.engine.identity.UserSearchDescriptor
import org.bonitasoft.engine.search.SearchOptionsBuilder

/**
 * Permite obtener el correo del estudiante solicitante o del funcionario 
 * encargado de la revisión.
 * 
 * @author ajcm
 * @version 1.0
 */
class CorreoDestinatario {

	private static final Logger logger = Logger.getLogger(CorreoDestinatario.class.toString())

	/**
	 * Obtiene el correo del contacto profesional del estudiante solicitante
	 * (iniciador de la instancia del proceso).
	 * 
	 * @param processAPI
	 * @param identityAPI
	 * @param processInstanciaId
	 * @return correoDelEstudiante
	 */
	static String obtenerCorreoEstudianteSolicitante(ProcessAPI processAPI, IdentityAPI identityAPI, Long processInstanciaId) {
		try{
			Long userId = processAPI.getProcessInstance(processInstanciaId).getStartedBy()
			String emailOfTheProcessInitiator = identityAPI.getUserContactData(userId, false).getEmail()

			return emailOfTheProcessInitiator.trim()
		} catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado en la organización: " + e.getMessage())
		}
	}

	/**
	 * Obtener el correo profesional del revisor (el cual debe estar en el grupo "secretaria" el cual debe tener un usuario único),
	 * del estudiante solicitante para enviarle las notificaciones y recordatorios
	 * 
	 * @param identityAPI
	 * @return correoDelRevisor
	 */
	static String obtenerCorreoDelRevisor(IdentityAPI identityAPI) {
		try{
			long groupSecretaryId = identityAPI.getGroupByPath("/unl/facultad/cis/secretaria").id
			SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 1)
			builder.filter(UserSearchDescriptor.GROUP_ID, groupSecretaryId)
			long secretaryId = identityAPI.searchUsers(builder.done())result.get(0).id
			String secretaryProfessionalEmail = identityAPI.getUserContactData(secretaryId, false).getEmail()

			return secretaryProfessionalEmail.trim()
		} catch(GroupNotFoundException e){
			logger.severe("Grupo no encontrado en la organización: " + e.getMessage())
		}
	}

	/**
	 * Obtener el correo profesional del director encargado de realizar la tarea utilizando el processDefinitionId 
	 * y el nombre de la tarea, últil cuando el director no es quien inició el proceso.
	 * 
	 * @param processAPI
	 * @param identityAPI
	 * @param processDefinitionId
	 * @param taskName
	 * @return
	 */
	static  String getCorreoDelDirector(ProcessAPI processAPI, IdentityAPI identityAPI, Long processDefinitionId, String taskName) {
		if (processDefinitionId == null) {
			throw new Exception("Se debe enviar el processDefinitionId")
		}
		if (!taskName) {
			throw new Exception("Se debe enviar el nombre de la tarea")
		}

		try{
			// Este script esta diseñado para obtener un posible usuario encargado de ejecutar la futura tarea, y
			// no funciona cuando hay 2 ó más posibles usuarios, es decir el grupo de "coordinacion" debe tener un solo usuario.
			long userId = processAPI.getPossibleUsersOfHumanTask(processDefinitionId, taskName, 0, 1)get(0).getId()

			return identityAPI.getUserContactData(userId, false).getEmail().trim()

		} catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado en la organización: " + e.getMessage())
		}
	}

	/**
	 * Obtener el correo profesional del director de la carrera mediante el processInstanceId, útil cuando el director
	 * es quien inicia el proceso
	 * 
	 * @param processAPI
	 * @param processInstanceId
	 * @param identityAPI
	 * @return email
	 */
	static String obtenerCorreoDelDirector(ProcessAPI processAPI, Long processInstanceId, IdentityAPI identityAPI) {
		if (!processInstanceId) {
			throw new Exception("Se debe enviar el processInstanceId del funcionario")
		}

		String professionalEmail = ""
		try{
			long useriId = processAPI.getProcessInstance(processInstanceId).getStartedBy()
			professionalEmail = identityAPI.getUserContactData(useriId, false).getEmail().trim()
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado en la organización: " + e.getMessage())
		}

		return professionalEmail
	}
}
