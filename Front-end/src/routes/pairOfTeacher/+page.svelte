<script>
	const urlAPI = process.env.URLAPI;
	import PairOfTeacher from './PairOfTeacher.svelte';
	import { goto } from '$app/navigation';
	import Navbar from '../Navbar.svelte';
	import { invalidateAll } from '$app/navigation';
	import { toast } from '@zerodevx/svelte-toast';

	export let data;
	import Cookies from 'js-cookie';

	$: if (data.status === 401) {
		goto('/auth', { invalidateAll: true });
	}

	$: pairTeacherData = data.pairTeacherData;
	let teacherData = data.teacherData;
	let userInfo = data.userInfo;
	let teacherDev = [];
	let teacherInfrastructure = [];
	let teacher1;
	let teacher2;

	for (let teacher of teacherData) {
		let hasModelling = false;
		let hasDevelopment = false;

		for (let speciality of teacher.specialities) {
			if (speciality.nameSpeciality.includes('MOD')) {
				hasModelling = true;
			} else if (speciality.nameSpeciality.includes('DEV')) {
				hasDevelopment = true;
			}
			if (speciality.nameSpeciality.includes('INF')) {
				teacherInfrastructure.push(teacher);
			}
		}

		if ((hasModelling && !hasDevelopment) || (hasDevelopment && !hasModelling)) {
			teacherDev.push(teacher);
		} else if (hasModelling && hasDevelopment && !teacherDev.includes(teacher)) {
			teacherDev.push(teacher);
		}
	}

	async function pairTeachers() {
		await fetch(urlAPI + `/teacher/pairTeachers`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
				Authorization: 'Bearer ' + Cookies.get('token')
			},
			body: JSON.stringify({
				idTeacher1: teacher1,
				idTeacher2: teacher2
			})
		});
		invalidateAll();
		toast.push('Les professeurs ont été liés !', {
			theme: {
				'--toastBackground': '#000000',
				'--toastProgressBackground': '#00a66b',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});
	}

	$: isSameTeacher = teacher1 === teacher2;
</script>

<Navbar {userInfo} />
<div class="container">
	<div class="list-pair-teacher">
		<div>
			<h2>Liste des binômes d'enseignants</h2>
		</div>
		{#each pairTeacherData as pairOfTeacher}
			<PairOfTeacher
				name0={pairOfTeacher[0].name}
				name1={pairOfTeacher[1].name}
				surname0={pairOfTeacher[0].surname}
				surname1={pairOfTeacher[1].surname}
			/>
		{/each}
	</div>

	{#if userInfo.role === 'ROLE_PLANNING_ASSISTANT'}
		<div class="pair-teacher-creation">
			<h2>Créer un binôme d'enseignants</h2>
			<div class="dropdowns">
				<div class="dropdown">
					<label for="teacher1">Professeur Dev:</label>
					<select bind:value={teacher1} class="select-teacher">
						{#each teacherDev as teacher}
							<option value={teacher.id}>{teacher.name} {teacher.surname}</option>
						{/each}
					</select>
				</div>
				<div class="dropdown">
					<label for="teacher2">Professeur Infra:</label>
					<select bind:value={teacher2} class="select-teacher">
						{#each teacherInfrastructure as teacher}
							<option value={teacher.id}>{teacher.name} {teacher.surname}</option>
						{/each}
					</select>
				</div>
			</div>
			<button class="smallButton" disabled={isSameTeacher} on:click={pairTeachers}
				>Lier les professeurs</button
			>
		</div>
	{/if}
</div>

<style>
	.container {
		display: flex;
		flex-direction: column;
		width: 100%;
		justify-content: center;
		align-items: center;
		margin: 50px 0;
	}

	.select-teacher {
		padding: 1% 0;
		text-align: center;
		border-radius: 10px;
	}

	.list-pair-teacher {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		width: 40%;
		border-radius: 30px;
		background-color: white;
		color: black;
		padding: 2% 10% 4% 10%;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
		margin-bottom: 5%;
	}

	.pair-teacher-creation {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		border-radius: 20px;
		padding: 2% 7% 4% 7%;
		box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
		background-color: white;
		color: black;
		gap: 15px;
	}
	.smallButton {
		padding: 5% 5%;
		text-align: center;
		border-radius: 10px;
		width: 80%;
		color: white;
		background-color: #3366ff;
		border: none;
		font-size: 1em;
	}

	.smallButton:disabled {
		background-color: #cccccc;
		color: #666666;
		cursor: not-allowed;
	}

	.smallButton:not(:disabled):hover {
		background-color: #7144ec;
		cursor: pointer;
		transform: translateY(-2px);
	}

	.dropdown {
		margin-bottom: 5px;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: space-between;
	}
	.dropdowns {
		margin-bottom: 10px;
		width: 85%;
	}
</style>
