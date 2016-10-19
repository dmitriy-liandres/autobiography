<div ng-controller="UserController">
    <input type="hidden" id="lang-input-id" value="${getServerLang()}">
    <table>
        <tr>
            <td valign="top">
                <#include "widgets/left-panel.ftl">
            </td>
            <td valign="top">
            <p>${message("autobioFull.description")}</p>
                <form action="autobiography-full.ftl" method="post">
                    <textarea name="autobioText" id="autobioText" rows="10" cols="80" maxlength="1000">
                    </textarea>
                </form>
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