<div ng-controller="UserController">
    <table>
        <tr>
            <td valign="top">
            <#include "widgets/left-panel.ftl">
            </td>
            <td valign="top">
                <form name="profileForm">
                    <table>
                        <tr>
                            <td><p>${message("name")}:</p></td>
                            <td><p>{{profile.name}}</p></td>
                        </tr>
                        <tr>
                            <td><p>${message("surname")}:</td>
                            <td><p>{{profile.surname}}</p></td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</div>