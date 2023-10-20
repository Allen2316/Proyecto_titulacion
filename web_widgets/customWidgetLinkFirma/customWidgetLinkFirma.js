(function () {
  try {
    return angular.module('bonitasoft.ui.widgets');
  } catch(e) {
    return angular.module('bonitasoft.ui.widgets', []);
  }
})().directive('customWidgetLinkFirma', function() {
    return {
      template: '<div class="text-{{ properties.alignment }}">\n    <a\n        ng-class="properties.buttonStyle !== \'none\' ? \'btn btn-\' + properties.buttonStyle : \'\'"\n        ng-href="{{ properties.targetUrl }}"\n        target="{{ properties.target }}"\n        ng-style="{{ properties.customStyle }}"\n        title="{{ properties.tooltipText }}">\n        <i\n            class="glyphicon glyphicon-{{ properties.iconClass }}"\n            ng-if="properties.displayIcon">\n        </i> \n        {{ properties.text | uiTranslate }}\n    </a>\n</div>'
    };
  });
