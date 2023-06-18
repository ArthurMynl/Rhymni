<script>
	const urlAPI = process.env.URLAPI;
	let date, hours, idRoom, idTeacher, idTeam;
	import Cookies from 'js-cookie';
	import { toast } from '@zerodevx/svelte-toast';
	export let presentation;
	export let rooms;
	export let teachers;
	let showForm = false;
	import { createEventDispatcher } from 'svelte';
	const dispatch = createEventDispatcher();

	function modifyPresentation() {
		if (presentation.type == 'Soutenance intermédiaire') {
			fetch(urlAPI + `/planning/modifyIntermediatePresentation`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Authorization: 'Bearer ' + Cookies.get('token')
				},
				body: JSON.stringify({
					idPresentation: presentation.idPresentation,
					date: date,
					hours: hours,
					idRoom: idRoom.numberRoom,
					idTeacher: idTeacher.id
				})
			})
				.then((response) => {
					if (response.status === 200) {
						window.location.reload();
					} else if (response.status === 400) {
						throw new Error('Le professeur a déjà une soutenance à cette date.');
					} else if (response.status === 409) {
						throw new Error('Une soutenance intermédiaire doit être avant la soutenance finale.');
					} else {
						throw new Error("Une erreur inattendue s'est produite.");
					}
				})
				.catch((error) => {
					toast.push(error.message, {
						theme: {
							'--toastBackground': '#ff0000',
							'--toastProgressBackground': '#ff0000',
							'--toastProgressAfterBackground': '#ff0000',
							'--toastIconFill': '#fff',
							'--toastBorderRadius': '15px'
						},
						pausable: true
					});
				});
		} else if (presentation.type == 'Soutenance finale') {
			fetch(urlAPI + `/planning/modifyFinalPresentation`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
					Authorization: 'Bearer ' + Cookies.get('token')
				},
				body: JSON.stringify({
					idPresentation: presentation.idPresentation,
					date: date,
					hours: hours,
					idRoom: idRoom.numberRoom,
					idTeacher: idTeacher.id
				})
			})
				.then((response) => {
					if (response.status === 200) {
						window.location.reload();
					} else if (response.status === 400) {
						throw new Error('Le professeur a déjà une soutenance à cette date.');
					} else if (response.status === 409) {
						throw new Error('Une soutenance finale doit être après la soutenance intermédiaire.');
					} else {
						throw new Error("Une erreur inattendue s'est produite.");
					}
				})
				.catch((error) => {
					console.log(error);
					toast.push(error.message, {
						theme: {
							'--toastBackground': '#ff0000',
							'--toastProgressBackground': '#ff0000',
							'--toastProgressAfterBackground': '#ff0000',
							'--toastIconFill': '#fff',
							'--toastBorderRadius': '15px'
						},
						pausable: true
					});
				});
		}
	}

	let displayedTeams = [];

	async function updateDisplayedTeams(teamNumber) {
		displayedTeams.push(teamNumber);
		displayedTeams.push(teamNumber.linkedTeamNumber);
	}
</script>

<div class="container" id="presentationCard">
	{#if showForm}
		<div class="modify-presentation">
			<div class="group">
				<input type="date" id="presentationDateModify" bind:value={date} />
				<input type="time" id = presentationTimeModify bind:value={hours} />
			</div>
			<div class="group">
				<select id="presentationRoomModify" bind:value={idRoom}>
					{#each rooms as room}
						<option value={room}>
							{room.name}
						</option>
					{/each}
				</select>
				<select id="presentationTeacherModify" bind:value={idTeacher}>
					{#each teachers as teacher}
						<option value={teacher[0]}>
							{teacher[0].surname}
							{teacher[0].name} -
							{teacher[1].surname}
							{teacher[1].name}
						</option>
					{/each}
				</select>
			</div>
			<div class="group">
				<button class="smallButton" id="modifyPresentation" on:click={modifyPresentation}> Modifier</button>
				<button
					class="smallButton"
					on:click={() => {
						showForm = false;
						dispatch('switchDeleteButton');
					}}>Annuler</button
				>
			</div>
		</div>
	{/if}
	{#if !showForm}
		<div class="group">
			<button id="modifyPresentation"
				class="smallButton"
				on:click={() => {
					showForm = true;
					dispatch('switchDeleteButton');
				}}>Modifier la soutenance</button
			>
			<button id="deletePresentation"
				class="smallButton"
				on:click={() => dispatch('deletePresentation', { id: presentation.idPresentation })}
			>
				Supprimer la soutenance
			</button>
		</div>
	{/if}
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 100%;

		.smallButton {
			padding: 10px;
			text-align: center;
			border-radius: 10px;
			width: 100%;
			color: white;
			background-color: #3366ff;
			border: none;
			font-size: 1em;
		}

		.modify-presentation {
			display: flex;
			flex-direction: column;
			justify-content: center;
			width: 100%;
			gap: 10px;

			.group {
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				width: 100%;
				gap: 10px;

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
		.group {
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			width: 100%;
			gap: 10px;
		}
	}
</style>
