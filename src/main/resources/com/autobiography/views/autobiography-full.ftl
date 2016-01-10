<div>
    <input type="hidden" id="lang-input-id" value="${getServerLang()}">
    <table>
        <tr>
            <td>
            <#include "widgets/left-panel.ftl">
            </td>
            <td>
            ${message("autobioFull.description")}
                <form action="autobiography-full.ftl" method="post">
                    <textarea name="autobioText" id="autobioText" rows="10" cols="80">
                    </textarea>
                </form>
            </td>
        </tr>
        <tr>
            <td>
                <button type="button" class="btn btn-primary"
                        ng-click="saveAutobioFull()">${message("save")}</button>
            </td>
        </tr>
    </table>
</div>