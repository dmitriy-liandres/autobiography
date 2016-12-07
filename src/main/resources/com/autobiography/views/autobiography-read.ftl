<div ng-controller="UserController">
    <table>
        <tr>
            <td valign="top">
            <#include "widgets/left-panel.ftl">
            </td>
            <td valign="top">
                <p id="current-user-name-id">${message("autobio.autobiographyAbout")} <b>{{currentUserName}}</b></p>
                <p class="whiteBackground" ng-bind-html="autobioText"></p>
                <input type="hidden" id="noAutobiographyId" value="${message("autobio.noAutobiography")}">
                <input type="hidden" id="autoBioTextTypeId" value="${autoBioTextType}">

            </td>
        </tr>
    </table>
</div>