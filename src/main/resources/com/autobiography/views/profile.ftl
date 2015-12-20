<div>
    <table>
        <tr>
            <td>
            <#include "widgets/left-panel.ftl">
            </td>
            <td>
                <form name="profileForm">
                    <table>
                        <tr>
                            <td>Name:</td>
                            <td><input type="text" name="name" id="name" ng-model="profile.name" value="${(profile.name)!''}"
                                       ng-required="true" ng-maxlength="100"></td>
                        </tr>
                        <tr>
                            <td>Surname:</td>
                            <td><input type="text" name="surname" id="surname"  ng-model="profile.surname"
                                       value="${(profile.surname)!''}" ng-required="true" ng-maxlength="100"></td>
                        </tr>
                    </table>
                    <button type="button" class="btn btn-primary" ng-click="updateProfile(profileForm)">Update</button>
                </form>
            </td>
        </tr>
    </table>
</div>