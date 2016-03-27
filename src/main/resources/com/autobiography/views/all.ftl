<div>
    <table>
        <tr ng-repeat="personGroup in personGroups">
            <td ng-if="personGroup.length >=0"><a href="autobiography-for-work/{{personGroup[0].id}}">{{getName(personGroup[0])}}</a></td>
            <td ng-if="personGroup.length >=1"><a
                    href="autobiography-for-work/{{personGroup[1].id}}">{{getName(personGroup[1])}}</a></td>
            <td ng-if="personGroup.length >=2"><a
                    href="autobiography-for-work/{{personGroup[2].id}}">{{getName(personGroup[2])}}</a></td>
        </tr>
    </table>
</div>