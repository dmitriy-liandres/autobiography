<ul>
    <#if loggedInPersonId??>
        <li><a href="profile">${message("leftPanel.profile")}</a></li>
    </#if>
    <li><a href="autobiography-for-work/{{idForLeftMenu}}">${message("leftPanel.autobiography-for-work")}{{currentUserName}}</a></li>
    <li><a href="autobiography-full/{{idForLeftMenu}}">${message("leftPanel.autobiography-full")}{{currentUserName}}</a></li>
    <li><a href="autobiography-interesting/{{idForLeftMenu}}">${message("leftPanel.autobiography-interesting")}{{currentUserName}}</a>
    </li>
</ul>