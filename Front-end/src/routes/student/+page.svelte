<script>
	export let data;
	import StudentImport from './StudentImport.svelte';
	import Navbar from '../Navbar.svelte';
	const urlAPI = process.env.URLAPI;
	let dataStudents = data.dataStudents;
	let dataTeam = data.dataTeam;
	let userInfo = data.userInfo;
	import Cookies from 'js-cookie';

	import { goto, invalidateAll } from '$app/navigation';

	const token = Cookies.get('token');
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};

	$: if (typeof window !== 'undefined') {
		if (data.status === 401) {
			goto('/auth', { invalidateAll: true });
		}
	}

	let students = [];
	let teams = [];
	let filteredStudents = [];
	let selectedTeam = '';
	let search = '';

	students = dataStudents;
	teams = dataTeam;
	filteredStudents = students;

	function filterStudents() {
		filteredStudents = students.filter((student) => {
			let teamFilter = true;
			let nameFilter = true;

			if (selectedTeam) {
				teamFilter = student.idTeam === Number(selectedTeam);
			}

			if (search) {
				nameFilter =
					student.name.toLowerCase().includes(search.toLowerCase()) ||
					student.surname.toLowerCase().includes(search.toLowerCase());
			}

			return teamFilter && nameFilter;
		});
	}

	async function refresh() {
		const [studentsResponse, teamResponse] = await Promise.all([
			fetch(urlAPI + '/student/getStudents', {
				method: 'GET',
				headers: headersList
			}),
			fetch(urlAPI + '/team/getTeams', {
				method: 'GET',
				headers: headersList
			})
		]);
		students = await studentsResponse.json();
		teams = await teamResponse.json();
		filterStudents();
	}
</script>

<Navbar {userInfo} />
<div class="filters">
	<div class="team-filter">
		<h2>Filtre par équipe :</h2>
		<select bind:value={selectedTeam} on:change={() => filterStudents()}>
			<option value="">Tous les étudiants</option>
			{#each teams as team}
				{#if team.number === 0}
					<option value="0">Équipe 0</option>
				{:else}
					<option value={team.number}>Équipe {team.number}</option>
				{/if}
			{/each}
		</select>
	</div>
	<div class="name-filter">
		<h2>Filtre par nom :</h2>
		<input type="text" bind:value={search} on:input={() => filterStudents()} />
	</div>
</div>

<div class="student-list">
	<h1>Liste des étudiants</h1>
	<ul>
		<div class="container">
			{#each filteredStudents as student}
				{#if student.idTeam === null}
					<li><a href="/student/{student.id}">{student.name} {student.surname} (aucune équipe)</a></li>
				{:else}
					<li><a href="/student/{student.id}">{student.name} {student.surname} (Équipe {student.idTeam})</a></li>
				{/if}
			{/each}
		</div>
	</ul>
	{#if userInfo.role === 'ROLE_PLANNING_ASSISTANT' || userInfo.role === 'ROLE_ADMIN'}
		<StudentImport on:import={refresh} />
	{/if}
</div>

<style lang="scss">
	.filters {
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;
		border-radius: 50px;
		background-color: white;
		color: black;
		padding: 2% 10% 4% 10%;
		margin: 50px 10% 3% 10%;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);

		.team-filter,
		.name-filter {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			gap: 15px;

			input,
			select {
				width: 100%;
				height: 40px;
				border-radius: 10px;
				border: 1px solid #c4c4c4;
				padding: 10px;
			}
		}
	}

	.student-list {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		border-radius: 50px;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
		padding: 2% 10% 4% 10%;
		margin: 0 10% 5% 10%;
		background-color: white;
		color: black;

		h1 {
			margin-bottom: 30px;
		}
	}

	.student-list ul {
		list-style-type: none;
		margin-bottom: 15px;
	}

	.student-list ul li {
		line-height: 1.6rem;
	}

	.student-list ul li a {
		text-decoration: underline;
		color: black;;
	}

	.container {
		column-count: 3;
		column-gap: 5rem;
	}

	@media screen and (max-width: 1500px) {
		.container {
			column-count: 2;
			column-gap: 10rem;
		}
	}

	@media screen and (max-width: 1100px) {
		.container {
			column-gap: 5rem;
		}

		.student-list {
			padding: 2% 5% 4% 5%;
		}
	}

	@media screen and (max-width: 800px) {
		.container {
			column-count: 1;
		}
	}
</style>
