<script>
	export let presentation;
	export let userInfo;

	import Cookies from 'js-cookie';
	import Mark from './Mark.svelte';
	const urlAPI = process.env.URLAPI;

	let markInput = presentation.mark;
	let markInDB = presentation.mark;
	$: modifyMarkMode = false;

	function toggleModifyMark() {
		modifyMarkMode = !modifyMarkMode;
		markInput = markInDB;
	}

	async function sendMarkToDB() {
		await fetch(urlAPI + `/teacher/setMarkTeam`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
				Authorization: 'Bearer ' + Cookies.get('token')
			},
			body: JSON.stringify({
				idPresentation: presentation.idPresentation,
				mark: markInput
			})
		});
		markInDB = markInput; // Maybe check if the request was successful
		toggleModifyMark();
	}
</script>

<div class="container">
	{#if !modifyMarkMode}
		<Mark mark={markInDB} maxmark={20} />
		{#if userInfo.role === 'ROLE_TEACHER' || userInfo.role === 'ROLE_JURY_MEMBER' || userInfo.role === 'ROLE_OPTION_LEADER'}
			<button on:click={toggleModifyMark}>Modifier</button>
		{/if}
	{:else}
		<input
			class="input-mark"
			type="number"
			min="0"
			max="20"
			step="0.5"
			placeholder={presentation.mark}
			bind:value={markInput}
		/>
		<span class="mark-max">/20</span>
		<button on:click={sendMarkToDB} disabled={markInput === null}>Valider</button>
		<button on:click={toggleModifyMark}>Annuler</button>
	{/if}
</div>

<style>
	.input-mark {
		font-size: 20px;
	}
	.mark-max {
		font-size: 15px;
		color: gray;
	}
</style>
