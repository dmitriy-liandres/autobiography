<div ng-controller="UserController">
    <input type="hidden" id="lang-input-id" value="${getServerLang()}">
    <table>
        <tr>
            <td valign="top">
            <#include "widgets/left-panel.ftl">
            </td>
            <td valign="top">
            <p>${message("autobioforwork.description")}</p>
            <select ng-model="templateSelection.value" ng-change="setTemplate()">
                <option ng-repeat="autoBioTemplate in autoBioTemplates" value="{{autoBioTemplate.id}}">{{autoBioTemplate.name}}</option>
            </select>
                <p>&nbsp;</p>
                <table cellpadding="2px">
                    <tr>
                        <td rowspan="2" valign="top" width="65%">
                            <form action="autobiography-for-work.ftl" method="post">
                                <textarea name="autobioText" id="autobioText" rows="10" cols="80" maxlength="1000">
                                </textarea>
                            </form>
                        </td>
                        <td valign="top" width="35%">
                        <p>${message("autobioforwork.tips")}</p>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <div class="form-group">
                    <button type="button" class="btn btn-primary"
                            ng-click="saveAutobioText()">${message("save")}</button>
                </div>
            </td>
        </tr>
    </table>
</div>