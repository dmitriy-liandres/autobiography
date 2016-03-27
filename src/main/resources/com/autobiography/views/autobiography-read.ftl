<div ng-controller="UserController">
    <table>
        <tr>
            <td>
            <#include "widgets/left-panel.ftl">
            </td>
            <td>
                <p ng-bind-html="autobioText"></p>
                <input type="hidden" id="noAutobiographyId" value="${message("autobio.noAutobiography")}">
                <input type="hidden" id="autoBioTextTypeId" value="${autoBioTextType}">

            </td>
        </tr>
    </table>
</div>