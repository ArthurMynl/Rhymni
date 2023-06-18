<script>
	const urlAPI = process.env.URLAPI;
	import Cookies from 'js-cookie';
	import { invalidateAll } from '$app/navigation';
	import Navbar from '../../Navbar.svelte';
	import { toast } from '@zerodevx/svelte-toast';	
	import Validation from './Validation.svelte';

	export let data;
	import TeacherWithoutButton from '../TeacherWithoutButton.svelte';
	$: teacher = data.teacher;
	let userInfo = data.userInfo;

	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: 'Bearer ' + Cookies.get('token')
	};

	$: if (typeof window !== 'undefined') {
		if (data.status === 401) {
			goto('/auth', { invalidateAll: true });
		}
	}

	async function addSpeciality(idSpecialityAdd) {
		await fetch(urlAPI + `/teacher/addSpeciality`, {
			method: 'POST',
			headers: headersList,
			body: JSON.stringify({
				specialityId: idSpecialityAdd,
				idTeacher: teacher.id
			})
		});
		invalidateAll();
		
        toast.push('La spécialité a bien été ajoutée', {
			theme: {
				'--toastBackground': '#000000',
				'--toastProgressBackground': '#3366ff',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});
	}

	async function removeSpeciality(idSpecialityRemove) {
		await fetch(urlAPI + `/teacher/removeSpeciality`, {
			method: 'POST',
			headers: headersList,
			body: JSON.stringify({
				specialityId: idSpecialityRemove,
				idTeacher: teacher.id
			})
		});
		invalidateAll();
            
		toast.push('La spécialité a bien été retirée', {
			theme: {
				'--toastBackground': '#000000',
				'--toastProgressBackground': '#eb4e3f',
				'--toastBorderRadius': '15px'
			},
			pausable: true
		});
	}

	const allSpecialities = [
		['INF', 0],
		['DEV', 1],
		['MOD', 2]
	];

	let selectedSpecialities = {};
	let missingSpecialities;

	$: {
		// Initialize or update selectedSpecialities
		selectedSpecialities = {};
		teacher.specialities.forEach((speciality) => {
			selectedSpecialities[speciality.idSpeciality] = [
				speciality.nameSpeciality,
				speciality.idSpeciality
			];
		});

		// Then update missingSpecialities
		missingSpecialities = allSpecialities.filter(
			(speciality) => !(speciality[1] in selectedSpecialities)
		);
	}

	//&& teacher.specialitiesStatus!='VALIDATED'
</script>

<Navbar {userInfo} />
<div class="container">
	<TeacherWithoutButton {teacher} />		
	<Validation {teacher}{userInfo} />

	{#if teacher.id === userInfo.userId && teacher.specialitiesStatus!='VALIDATED' } 
		<div class="modify-speciality">
			<div class="add-speciality">
				<h2>Ajouter des spécialités</h2>
				{#each missingSpecialities as missingSpeciality}
					<button class="smallButton" on:click={() => addSpeciality(missingSpeciality[1])}>
						{#if missingSpeciality[0] === 'MOD'}
							Ajouter modélisation
						{:else if missingSpeciality[0] === 'DEV'}
							Ajouter développement
						{:else if missingSpeciality[0] === 'INF'}
							Ajouter infrastructure
						{/if}
					</button>
				{/each}
			</div>
			<div class="remove-speciality">
				<h2>Retirer des spécialités</h2>
				{#each teacher.specialities as speciality}
					<button class="smallButton" on:click={() => removeSpeciality(speciality.idSpeciality)}>
						{#if speciality.nameSpeciality === 'MOD'}
							Retirer modélisation
						{:else if speciality.nameSpeciality === 'DEV'}
							Retirer développement
						{:else if speciality.nameSpeciality === 'INF'}
							Retirer infrastructure
						{/if}
					</button>
				{/each}
			</div>
		</div>
	{/if}
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		margin: 50px 0;
		gap: 30px;

		.modify-speciality {
			display: flex;
			flex-direction: row;
			border-radius: 50px;
			align-items: flex-start;
			justify-content: space-around;
			width: 50%;
			padding: 50px;
			background-color: white;
			color: black;
			box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);

			.add-speciality, .remove-speciality {
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: space-around;
				width: 45%;
				gap: 20px;
			}
		}
	}

	.smallButton {
		padding: 5% 5%;
		text-align: center;
		width: 100%;
		border-radius: 10px;
		color: white;
		background-color: #3366ff;
		border: none;
		font-size: 1em;

		&:hover {
			cursor: pointer;
			background-color: #2b5ffb;
			transform: translateY(-2px);
		}
	}
</style>
