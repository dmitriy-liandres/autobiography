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
                            <td colspan="2">
                            ${message("welcomeMain")}
                            </td>
                        </tr>
                        <tr>
                            <td>${message("name")}:</td>
                            <td><input type="text" name="name" id="name" ng-model="profile.name"
                                       value="${(profile.name)!''}"
                                       ng-required="true" ng-maxlength="100"></td>
                        </tr>
                        <tr>
                            <td>${message("surname")}:</td>
                            <td><input type="text" name="surname" id="surname" ng-model="profile.surname"
                                       value="${(profile.surname)!''}" ng-required="true" ng-maxlength="100"></td>
                        </tr>
                        <tr>
                            <td>${message("is_public")}:</td>
                            <td><input type="checkbox" name="isPublic" id="isPublic" ng-model="profile.isPublic"
                                       value="${(profile.isPublic)!"true"}"></td>
                        </tr>
                    </table>
                    <button type="button" class="btn btn-primary"
                            ng-click="updateProfile(profileForm)">${message("update")}</button>
                </form>
            </td>
        </tr>
    </table>
</div>