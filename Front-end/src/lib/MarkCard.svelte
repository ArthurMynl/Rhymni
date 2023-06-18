<script>
	export let mark;
	export let maxMark;
	export let text;
	export let canModify;
	export let isBonus;
	export let totalMark;
	export let idStudent;
	export let idTeam;
	export let noteType;

	import { toast } from '@zerodevx/svelte-toast';
	import Cookies from 'js-cookie';
	import { invalidateAll } from '$app/navigation';

	let editMode = false;
	let newMark;

	const changeEditMode = () => {
		editMode = !editMode;
		if (!editMode) {
			newMark = undefined; // Réinitialise la nouvelle note en cas d'annulation
		}
	};

	const validateMark = async () => {
		if (!newMark) {
			toast.push('Veuillez entrer une note', {
				theme: {
					'--toastBackground': '#ff0000',
					'--toastProgressBackground': '#ff0000',
					'--toastProgressAfterBackground': '#ff0000',
					'--toastIconFill': '#fff',
					'--toastBorderRadius': '15px'
				}
			});
			return;
		}

		const updatedMark = newMark;
		if (!isBonus) {
			if (updatedMark < 0 || updatedMark > maxMark) {
				toast.push('La note doit être comprise entre 0 et ' + maxMark, {
					theme: {
						'--toastBackground': '#ff0000',
						'--toastProgressBackground': '#ff0000',
						'--toastProgressAfterBackground': '#ff0000',
						'--toastIconFill': '#fff',
						'--toastBorderRadius': '15px'
					}
				});
				return;
			}
		} else {
			if (updatedMark < -totalMark || updatedMark > maxMark - totalMark) {
				toast.push(
					'La note doit être comprise entre ' + -totalMark + ' et ' + (maxMark - totalMark),
					{
						theme: {
							'--toastBackground': '#ff0000',
							'--toastProgressBackground': '#ff0000',
							'--toastProgressAfterBackground': '#ff0000',
							'--toastIconFill': '#fff',
							'--toastBorderRadius': '15px'
						}
					}
				);
				return;
			}
		}

		mark = updatedMark;
		editMode = false;

		const urlAPI = process.env.URLAPI;

		const headers = {
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + Cookies.get('token')
		};

		try {
			if (noteType === 'markPresentation') {
				const teamRequest = {
					teamNumber: idTeam,
					markPresentation: updatedMark
				};

				await fetch(`${urlAPI}/team/setMarkTeam`, {
					method: 'PUT',
					headers,
					body: JSON.stringify(teamRequest)
				});
			} else if (noteType === 'markValidation') {
				const teamRequest = {
					teamNumber: idTeam,
					markValidation: updatedMark
				};

				await fetch(`${urlAPI}/team/setMarkTeam`, {
					method: 'PUT',
					headers,
					body: JSON.stringify(teamRequest)
				});
			} else if (noteType === 'studentMark') {
				const teamRequest = {
					id: idStudent,
					studentMark: updatedMark
				};

				await fetch(`${urlAPI}/student/setStudentMarks`, {
					method: 'PUT',
					headers,
					body: JSON.stringify(teamRequest)
				});
			} else if (noteType === 'optionLeaderBonus') {
				const teamRequest = {
					id: idStudent,
					optionLeaderBonus: updatedMark
				};

				await fetch(`${urlAPI}/student/setStudentMarks`, {
					method: 'PUT',
					headers,
					body: JSON.stringify(teamRequest)
				});
			} else if (noteType === 'juryBonus') {
				const teamRequest = {
					id: idStudent,
					juryBonus: updatedMark
				};

				await fetch(`${urlAPI}/student/setStudentMarks`, {
					method: 'PUT',
					headers,
					body: JSON.stringify(teamRequest)
				});
			}
			invalidateAll();

			toast.push('La note a été mise à jour avec succès');
		} catch (error) {
			console.error('Erreur lors de la mise à jour de la note', error);
			toast.push('Erreur lors de la mise à jour de la note', {
				theme: {
					'--toastBackground': '#ff0000',
					'--toastProgressBackground': '#ff0000',
					'--toastProgressAfterBackground': '#ff0000',
					'--toastIconFill': '#fff',
					'--toastBorderRadius': '15px'
				}
			});
		}
	};
</script>

{#if !editMode}
	<button class="container" disabled={!canModify} on:click={changeEditMode}>
		<h3 class="text">{text}</h3>
		{#if isBonus && !isNaN(mark) && mark !== null && mark !== undefined}
			{#if mark >= 0}
				<span class="bonus">+{mark}</span>
			{:else}
				<span class="malus">{mark}</span>
			{/if}
		{:else if isNaN(mark) || mark === null || mark === undefined}
			<span class="mark">-</span>
		{:else}
			<span class="mark">{mark}</span>
		{/if}
		{#if !isBonus}
			<span class="max-mark">/ {maxMark}</span>
		{/if}
	</button>
{:else}
	<div class="container">
		<h3 class="text">{text}</h3>
		<input
			type="number"
			bind:value={newMark}
			min={isBonus ? -totalMark : 0}
			max={maxMark - totalMark}
			class="input-mark"
			placeholder="Entrez une note"
			disabled={!canModify}
		/>
		<div class="button-container">
			<button class="validate-button" on:click={validateMark}>Valider</button>
			<button class="cancel-button" on:click={changeEditMode}>Annuler</button>
		</div>
	</div>
{/if}

<style lang="scss">
	.container {
		all: unset;
		padding: 10px;
		border-radius: 15px;
		text-align: center;
		background-color: #e8e6ed;
		cursor: pointer;
		border: 1px solid black;

		&:disabled {
			cursor: default;
			border: none;
		}

		.mark {
			font-size: 20px;
		}

		.input-mark {
			width: 50%;
			height: 30px;
			border-radius: 5px;
			border: 1px solid black;
			text-align: center;
			font-size: 20px;
			margin-top: 10px;
		}

		.validate-button {
			all: unset;
			padding: 5px 10px;
			border-radius: 5px;
			background-color: #e8e6ed;
			cursor: pointer;
			border: 1px solid black;
			margin-left: 10px;
		}

		.cancel-button {
			all: unset;
			padding: 5px 10px;
			border-radius: 5px;
			background-color: #e8e6ed;
			cursor: pointer;
			border: 1px solid black;
			margin-left: 10px;
		}

		.max-mark {
			font-size: 15px;
			color: gray;
		}

		.button-container {
			margin-top: 10px;
		}
	}
</style>
