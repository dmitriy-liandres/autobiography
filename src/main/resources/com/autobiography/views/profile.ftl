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
                            <td colspan="2">
                            <p>${message("welcomeMain")}</p>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <div class="form-group">
                                    <label>${message("name")}:</label>
                                    <input type="text" class="form-control" name="name" id="name" ng-model="profile.name"
                                           value="${(profile.name)!''}"
                                           ng-required="true" ng-maxlength="100">
                                </div>
                                <div class="form-group">
                                    <label>${message("surname")}:</label>
                                    <input type="text" name="surname" class="form-control" id="surname" ng-model="profile.surname"
                                           value="${(profile.surname)!''}" ng-required="true" ng-maxlength="100">
                                </div>
                                <div class="form-group">
                                    <label>${message("is_public")}:</label>
                                    <input type="checkbox" class="form-control" name="isPublic" id="isPublic" ng-model="profile.isPublic"
                                           value="${(profile.isPublic)!"true"}">
                                </div>
                                <div class="form-group">
                                    <button type="button" class="btn btn-primary"
                                            ng-click="updateProfile(profileForm)">${message("update")}</button>
                                </div>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
</div>