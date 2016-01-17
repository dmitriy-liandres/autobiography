<div>
    <input type="hidden" id="lang-input-id" value="${getServerLang()}">
    <table>
        <tr>
            <td>
            <#include "widgets/left-panel.ftl">
            </td>
            <td>
            ${message("autobioforwork.description")}
                <select ng-model="templateSelection" ng-change="setTemplate()">
                    <option ng-repeat="autoBioTemplate in autoBioTemplates" value="{{autoBioTemplate.id}}">{{autoBioTemplate.name}}</option>
                </select>
                <table>
                    <tr>
                        <td width="50%">
                            <form action="autobiography-for-work.ftl" method="post">
                                <textarea name="autobioText" id="autobioText" rows="10" cols="80" maxlength="1000">
                                </textarea>
                            </form>
                        </td>
                        <td>
                        ${message("autobioforwork.tips")}
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