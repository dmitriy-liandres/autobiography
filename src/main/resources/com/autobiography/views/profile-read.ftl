<div ng-controller="UserController">
    <table>
        <tr>
            <td>
            <#include "widgets/left-panel.ftl">
            </td>
            <td>
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