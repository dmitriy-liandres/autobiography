<div ng-controller="UserController">
    <input type="hidden" id="lang-input-id" value="${getServerLang()}">
    <table>
        <tr>
            <td>
            <#include "widgets/left-panel.ftl">
            </td>
            <td>
            ${message("autobioforwork.description")}
                <select ng-model="templateSelection.value" ng-change="setTemplate()">
                    <option ng-repeat="autoBioTemplate in autoBioTemplates" value="{{autoBioTemplate.id}}">{{autoBioTemplate.name}}</option>
                </select>
                <table>
                    <tr>
                        <td rowspan="2" width="50%">
                            <form action="autobiography-for-work.ftl" method="post">
                                <textarea name="autobioText" id="autobioText" rows="10" cols="80" maxlength="1000">
                                </textarea>
                            </form>
                        </td>
                        <td height="50%">
                        ${message("autobioforwork.tips")}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p ng-bind-html="autoBioExample"></p>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <button type="button" class="btn btn-primary"
                        ng-click="saveAutobioText()">${message("save")}</button>
            </td>
        </tr>
    </table>
</div>