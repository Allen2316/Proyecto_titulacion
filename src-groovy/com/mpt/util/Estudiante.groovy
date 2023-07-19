package com.mpt.util

import java.util.logging.Logger

import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.identity.UserNotFoundException

import com.mpt.constantes.MPTConstants

/**
 * Obtiene ciertos datos de un estudiante
 * 
 * @author ajcm
 * @version 1.0
 */
class Estudiante {
	
	private static final Logger logger = Logger.getLogger(Estudiante.class.toString())

	/**
	 * Obtiene los nombres completos del estudiante solicitante (El cual inició el proceso) utilizando el idSolicitanteBonitaBPM (Es el userId).
	 * ObtenerEstudiante.obtenerNombre(apiAccessor.getIdentityAPI(), solicitud.idSolicitanteBonitaBPM)
	 * 
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM
	 * @return String
	 */
	static String getFullName(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM) {
		if (!idSolicitanteBonitaBPM) {
			throw new Exception("Se debe enviar el idSolicitanteBonitaBPM del estudiante solicitante getFullName")
		}

		String fullNameStudent = ""
		try{
			fullNameStudent = identityAPI.getUser(idSolicitanteBonitaBPM).firstName +
					" " + identityAPI.getUser(idSolicitanteBonitaBPM).lastName;
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado en la organización getFullName: " + e.getMessage())
		}

		return fullNameStudent.trim()
	}
	
	
	static String getFirstName(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM) {
		if (!idSolicitanteBonitaBPM) {
			throw new Exception("Se debe enviar el idSolicitanteBonitaBPM del estudiante solicitante getFirstName")
		}

		String firstNameStudent = ""
		try{
			firstNameStudent = identityAPI.getUser(idSolicitanteBonitaBPM).firstName					
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado en la organización getFirstName: " + e.getMessage())
		}

		return firstNameStudent.trim()
	}
	
	static String getLastName(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM) {
		if (!idSolicitanteBonitaBPM) {
			throw new Exception("Se debe enviar el idSolicitanteBonitaBPM del estudiante solicitante getLastName")
		}

		String lastNameStudent = ""
		try{
			lastNameStudent = identityAPI.getUser(idSolicitanteBonitaBPM).lastName
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado en la organización getLastName: " + e.getMessage())
		}
		return lastNameStudent.trim()
	}
	

	/**
	 * Obtiene la cédula del estudiante solicitante
	 * 
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM
	 * @return String
	 */
	static String getCedulaStudent(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM) {
		if (!idSolicitanteBonitaBPM) {
			throw new Exception("Se debe enviar el idSolicitanteBonitaBPM del estudiante solicitante getCedulaStudent")
		}

		String cedulaEstudiante = ""
		try{
			cedulaEstudiante = identityAPI.getCustomUserInfo(idSolicitanteBonitaBPM, 0, 1)
					.find { MPTConstants.IDENTIFICATION_DOCUMENT_TYPE == it.getDefinition().getName() }
					?.getValue();
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado en la organización: getCedulaStudent " + e.getMessage())
		}

		return cedulaEstudiante.trim()
	}
	/**
	 * Obtiene el ID del estudiante que inició el proceso, ideal cuando no se tiene acceso a la variable de negocio solicitud
	 * @param apiAccessor 
	 * @param processInstanceId
	 * @return El ID del estudiante
	 */
	static Long getId(APIAccessor apiAccessor, Long processInstanceId) {
		Long userId = 0;
		try{
			 userId = apiAccessor.getIdentityAPI().getUser(apiAccessor.getProcessAPI()
					.getProcessInstance(processInstanceId)
					.getStartedBy()).getId()							
		}catch(UserNotFoundException e){
			logger.severe("Usuario no encontrado: getId " + e.getMessage())
		}
		return userId;
	}
}