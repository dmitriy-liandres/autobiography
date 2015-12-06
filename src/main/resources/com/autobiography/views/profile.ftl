<div>
    <table>
        <tr>
            <td>
            <#include "widgets/left-panel.ftl">
            </td>
            <td>
                <table>
                    <tr>
                        <td>Name:</td>
                        <td><input type="text" name="name" ng-model="profile.name" value="${(profile.name)!''}" maxlength="255"></td>
                    </tr>
                    <tr>
                        <td>Surname:</td>
                        <td><input type="text" name="surname" ng-model="profile.surname" value="${(profile.surname)!''}" maxlength="255"></td>
                    </tr>
                </table>
                <button type="button" class="btn btn-primary" ng-click="updateProfile()">Update</button>
            </td>
        </tr>
    </table>
</div>