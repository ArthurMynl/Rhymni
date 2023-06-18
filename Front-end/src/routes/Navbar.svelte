<script>
	export let userInfo;
	import Cookie from 'js-cookie';
	import { goto } from '$app/navigation';

	function disconnect() {
		Cookie.remove('token');
		goto('/auth', { invalidateAll: true });
	}
</script>

{#if userInfo.role === 'ROLE_STUDENT'}
	<nav>
		<ul>
			<li><a href="/student/{userInfo.userId}" id="students">Mon profil</a></li>
			<li><a href="/project" id="projects">Projets</a></li>
			<li><a href="/teacher" id="teachers">Professeurs</a></li>
			<li><button on:click={disconnect}>Déconnexion</button></li>
		</ul>
	</nav>
{:else if userInfo.role === 'ROLE_TEAM_MEMBER'}
	<nav>
		<ul>
			<li><a href="/student/{userInfo.userId}" id="students">Mon profil</a></li>
			<li><a href="/project/{userInfo.idTeam}" id="project{userInfo.idTeam}">Mon projet</a></li>
			<li><a href="/project" id="projects">Projets</a></li>
			<li><a href="/teacher" id="teachers">Professeurs</a></li>
			<li><a href="/consulting" id="consultings">Consultings</a></li>
			<li><a href="/presentation" id="soutenances">Soutenances</a></li>
			<li><a href="/notification" id="notifications">Notifications</a></li>
			<li><button on:click={disconnect}>Déconnexion</button></li>
		</ul>
	</nav>
{:else if userInfo.role === 'ROLE_TEACHER' || userInfo.role === 'ROLE_JURY_MEMBER'}
	<nav>
		<ul>
			<li><a href="/teacher/{userInfo.userId}" id="teacher{userInfo.userId}">Mon profil</a></li>
			<li><a href="/project" id="projects">Projets</a></li>
			<li><a href="/student" id="students">Étudiants</a></li>
			<li><a href="/consulting" id="consultings">Consultings</a></li>
			<li><a href="/presentation" id="soutenances">Soutenances</a></li>
			<li><a href="/notification" id="notidications">Notifications</a></li>
			<li><button on:click={disconnect}>Déconnexion</button></li>
		</ul>
	</nav>
{:else if userInfo.role === 'ROLE_OPTION_LEADER'}
	<nav>
		<ul>
			<li><a href="/teacher/{userInfo.userId}" id="myProfile">Mon profil</a></li>
			<li><a href="/project" id="projects">Projets</a></li>
			<li><a href="/student" id="students">Étudiants</a></li>
			<li><a href="/teacher" id="teachers">Professeurs</a></li>
			<li><a href="/linkedTeams" id="linkedTeams">Lier des équipes</a></li>
			<li><a href="/consulting" id="consultings">Consultings</a></li>
			<li><a href="/presentation" id="soutenances">Soutenances</a></li>
			<li><a href="/notification" id="notifications">Notifications</a></li>
			<li><button on:click={disconnect}>Déconnexion</button></li>
		</ul>
	</nav>
{:else if userInfo.role === 'ROLE_PLANNING_ASSISTANT'}
	<nav>
		<ul>
			<li><a href="/project" id="projects">Projets</a></li>
			<li><a href="/student" id="students">Étudiants</a></li>
			<li><a href="/teacher" id="students">Professeurs</a></li>
			<li><a href="/pairOfTeacher" id="pairTeacher">Lier des Professeurs</a></li>
			<li><a href="/consulting" id="consultings">Consultings</a></li>
			<li><a href="/presentation" id="soutenances">Soutenances</a></li>
			<li><a href="/notification" id="notifications">Notifications</a></li>
			<li><button on:click={disconnect}>Déconnexion</button></li>
		</ul>
	</nav>
{/if}

<!-- 
<nav>
	<ul>
		<li><a href="/project/1">Mon projet</a></li>
		<li><a href="/project">Projets</a></li>
		<li><a href="/student">Étudiants</a></li>
		<li><a href="/teacher">Professeurs</a></li>
		<li><a href="/linkedTeams">Lier des équipes</a></li>
		<li><a href="/pairOfTeacher">Lier des Professeurs</a></li>
		<li><a href="/consulting">Consulting</a></li>
		<li><a href="/notification">Notifications</a></li>
		<li><a href="/presentation">Presentation</a></li>
	</ul>
</nav> 
-->

<style>
	nav {
		display: flex;
		justify-content: space-between;
		align-items: center;
		background-color: #3366ff;
		padding: 20px;
	}

	nav ul {
		display: flex;
		list-style: none;
		gap: 50px;
	}

	nav ul li a {
		color: white;
		text-decoration: none;
		font-size: 1.2rem;
	}

	nav ul li button {
		color: white;
		background-color: transparent;
		border: none;
		font-size: 1.2rem;
		cursor: pointer;
		transform: translateY(2px);
	}
</style>
