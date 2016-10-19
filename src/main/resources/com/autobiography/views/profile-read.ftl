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
                            <td>${message("name")}:</td>
                            <td>{{profile.name}}</td>
                        </tr>
                        <tr>
                            <td>${message("surname")}:</td>
                            <td>{{profile.surname}}</td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</div>