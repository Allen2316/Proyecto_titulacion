/**
 * 
 */
package com.mpt.payloads

import java.util.logging.Logger

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.identity.User

import com.mpt.constantes.CertificateFields
import com.mpt.constantes.MPTConstants

import groovy.json.JsonBuilder

/**
 * @author Allen
 *
 */
class Payload {

	private static final Logger logger = Logger.getLogger(Payload.class.toString())

	/**
	 * Permite obtener la cédula del funcionario responsable de firmar el documento.
	 * 
	 * @param processAPI
	 * @param identityAPI
	 * @param processDefinitionId
	 * @param taskName
	 * @return Numero de cédula
	 */
	private static String obtenerCedula(ProcessAPI processAPI, IdentityAPI identityAPI, Long processDefinitionId, String taskName) {
		if (!processDefinitionId) {
			throw new Exception("Se debe enviar el processDefinitionId")
		}
		if (!taskName) {
			throw new Exception("Se debe enviar el nombre de la tarea")
		}

		// Este script esta diseñado para obtener un posible usuario (candidato) encargado de ejecutar la futura tarea, y
		// no funciona cuando hay 2 ó más posibles usuarios.
		List<User> possibleUser = processAPI.getPossibleUsersOfHumanTask(processDefinitionId, taskName, 0, 1)

		if (possibleUser.size() > 1) {
			logger.severe("--> Existe más de un candidato para la tarea: " + taskName)
		} else if (possibleUser.isEmpty()) {
			logger.severe("--> No hay candidato para la tarea: " + taskName)
		}

		long userId = possibleUser[0].getId()
		String cedula = identityAPI.getCustomUserInfo(userId, 0, 1)
				.find { MPTConstants.IDENTIFICATION_DOCUMENT_TYPE == it.getDefinition().getName() }
				?.getValue()

		if (cedula.isEmpty()) {
			logger.severe("--> El funcionario no tiene cédula!")
		}

		return cedula.trim()
	}

	/**
	 * Permite obtener la cédula del estudiante responsable de firmar el documento.
	 *
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM
	 * @return String
	 */
	private static String getCedulaEstudiante(IdentityAPI identityAPI, Long idSolicitanteBonitaBPM) {
		if (!idSolicitanteBonitaBPM) {
			throw new Exception("Se debe enviar el idSolicitanteBonitaBPM del estudiante solicitante que va a firmar")
		}

		String cedulaEstudiante = identityAPI.getCustomUserInfo(idSolicitanteBonitaBPM, 0, 1)
				.find { MPTConstants.IDENTIFICATION_DOCUMENT_TYPE == it.getDefinition().getName() }
				?.getValue();

		if (cedulaEstudiante.isEmpty()) {
			logger.severe("--> El estudiante no tiene cédula!")
		}

		return cedulaEstudiante.trim()
	}

	/**
	 * Permite generar el payload a enviar al servicio web de firma
	 * 
	 * @param groupName indica a que grupo pertenece el usuario que requiere el payload
	 * @param pdfGenerado
	 * @param pdfFirmado
	 * @param processAPI
	 * @param identityAPI
	 * @param idSolicitanteBonitaBPM
	 * @param processDefinitionId
	 * @return String en formato json
	 */
	static String buildPayload(String groupName, Document pdfGenerado, Document pdfFirmado, ProcessAPI processAPI, IdentityAPI identityAPI,
			Long idSolicitanteBonitaBPM, Long processDefinitionId) {
		if (groupName.isEmpty()) {
			throw new Exception("Se debe enviar el groupName")
		}

		def jsonBuilder = new JsonBuilder()
		String futurePDFName = ""
		String pdfEncoded = ""
		String cedula = ""

		try {
			if (groupName.equals(CertificateFields.STUDENT_GROUP_NAME)) {
				// Si el estudiante va a firmar siempre se utiliza el pdfGenerado
				futurePDFName = getFutureFilenamePDF(pdfGenerado.getContentFileName());
				pdfEncoded = generarBase64(processAPI, pdfGenerado)
				cedula = getCedulaEstudiante(identityAPI, idSolicitanteBonitaBPM)
			}

			if (groupName.equals(CertificateFields.SECRETARY_GROUP_NAME)) {
				// Si la secretaria va a firmar siempre se utiliza el pdfGenerado
				futurePDFName = getFutureFilenamePDF(pdfGenerado.getContentFileName());
				pdfEncoded = generarBase64(processAPI, pdfGenerado)
				cedula = obtenerCedula(processAPI, identityAPI, processDefinitionId, MPTConstants.TASK_NAME_SIGN_CERTIFICATE_SECRETARY)
			}

			if (groupName.equals(CertificateFields.COORDINATION_GROUP_NAME)) {
				if (pdfFirmado == null) {
					futurePDFName = getFutureFilenamePDF(pdfGenerado.getContentFileName());
					pdfEncoded = generarBase64(processAPI, pdfGenerado)
				} else {
					futurePDFName = getFutureFilenamePDF(pdfFirmado.getContentFileName());
					pdfEncoded = generarBase64(processAPI, pdfFirmado)
				}

				cedula = obtenerCedula(processAPI, identityAPI, processDefinitionId, MPTConstants.TASK_NAME_SIGN_CERTIFICATE_GESTOR)
			}

			if (groupName.equals(CertificateFields.DECAN_GROUP_NAME)) {
				// El decano siempre recibe un pdfFirmado
				futurePDFName = getFutureFilenamePDF(pdfFirmado.getContentFileName());
				pdfEncoded = generarBase64(processAPI, pdfFirmado)
				cedula = obtenerCedula(processAPI, identityAPI, processDefinitionId, MPTConstants.TASK_NAME_SIGN_CERTIFICATE_DEAN)
			}

			jsonBuilder(cedula: "${cedula}",
			sistema: MPTConstants.SYSTEM_NAME,
			documentos: [[nombre: "${futurePDFName}", documento: "${pdfEncoded}"]])

			return jsonBuilder.toString()

		} catch (Exception e) {
			throw new Exception("Error: " + e)
		}
	}

	/**
	 * Permite codificar un archivo pdf en base64.
	 * 
	 * @param processAPI
	 * @param pdfFile
	 * @return
	 */
	private static String generarBase64(ProcessAPI processAPI, Document pdfFile) {
		if (!pdfFile) {
			throw new Exception("Se debe enviar el archivo pdf")
		}

		byte[] content = processAPI.getDocumentContent(pdfFile.getContentStorageId())
		String encodedFile = Base64.getEncoder().encodeToString(content)

		return encodedFile
	}

	/**
	 * 
	 * @param currentFilename
	 * @return
	 */
	private static String getFutureFilenamePDF(String currentFilename) {
		return currentFilename.replaceAll(MPTConstants.PDF_EXTENSION, MPTConstants.REPLACEMENT_PDF_EXTENSION)
	}
}