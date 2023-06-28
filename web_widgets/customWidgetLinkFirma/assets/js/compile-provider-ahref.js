/**
 * Permite registrar el protocolo firmaec en el navegador web Chrome, Firefox, Edge,... * 
 */
angular.module('bonitasoft.ui').config(function ($compileProvider) {
    // console.info('Se procede a registrar el protocolo firmaec para que angular no lo marque como: unsafed');
    $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|firmaec):/)
})