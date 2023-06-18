<script>
	export let presentation;
	export let userInfo;
	export let team0or1;
	import Mark from './Mark.svelte';
	import Cookies from 'js-cookie';
	let headersList = {
		'Content-Type': 'application/json',
		Authorization: 'Bearer ' + Cookies.get('token')
	};
	const urlAPI = process.env.URLAPI;

	$: modifCommentTeacherMode = false;
	$: commentTeacher1InBDD = presentation.commentTeacher1;
	$: commentTeacher2InBDD = presentation.commentTeacher2;
	let commentTeacherInput1 = presentation.commentTeacher1;
	let commentTeacherInput2 = presentation.commentTeacher2;

	$: modifCommentTeamMode = false;
	$: commentTeam1InBDD = presentation.commentTeam1;
	$: commentTeam2InBDD = presentation.commentTeam2;
	let commentTeamInput1 = presentation.commentTeam1;
	let commentTeamInput2 = presentation.commentTeam2;

	async function updateCommentTeacher() {
		const body = {
			idPresentation: presentation.idPresentation,
			commentTeacher1: null,
			commentTeacher2: null
		};
		if (team0or1 == 0) {
			body.commentTeacher1 = commentTeacherInput1;
		} else {
			body.commentTeacher2 = commentTeacherInput2;
		}
		await fetch(urlAPI + `/teacher/setPresentationCommentTeacher`, {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify(body)
		});
		if (team0or1 == 0) {
			presentation.commentTeacher1 = commentTeacherInput1;
			commentTeacher1InBDD = commentTeacherInput1;
		} else {
			presentation.commentTeacher2 = commentTeacherInput2;
			commentTeacher2InBDD = commentTeacherInput2;
		}
		modifCommentTeacherMode = false;
	}

	async function updateCommentTeam() {
		const body = {
			idPresentation: presentation.idPresentation,
			commentTeam1: null,
			commentTeam2: null
		};
		if (team0or1 == 0) {
			body.commentTeam1 = commentTeamInput1;
		} else {
			body.commentTeam2 = commentTeamInput2;
		}
		await fetch(urlAPI + `/team/setPresentationCommentTeam`, {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify(body)
		});
		if (team0or1 == 0) {
			presentation.commentTeam1 = commentTeamInput1;
			commentTeam1InBDD = commentTeamInput1;
		} else {
			presentation.commentTeam2 = commentTeamInput2;
			commentTeam2InBDD = commentTeamInput2;
		}
		modifCommentTeamMode = false;
	}
</script>

<div class="container">
	{#if userInfo.role === 'ROLE_TEACHER' || userInfo.role === 'ROLE_JURY_MEMBER' || userInfo.role === 'ROLE_PLANNING_ASSISTANT' || userInfo.role === 'ROLE_OPTION_LEADER'}
		<h1>Équipe {presentation.teams[team0or1].number}</h1>
	{:else if userInfo.idTeam == presentation.teams[team0or1].number}
		<h1>Votre équipe</h1>
	{:else}
		<h1>Équipe liée</h1>
	{/if}

	<div class="rubric">
		<h3>Commentaire professeur</h3>
		{#if new Date() > new Date(presentation.dateHours)}
			{#if !modifCommentTeacherMode}
				{#if team0or1 == 0}
					{#if commentTeacher1InBDD === null}
						<p>-</p>
					{:else}
						<p>{commentTeacher1InBDD}</p>
					{/if}
				{:else if commentTeacher2InBDD === null}
					<p>-</p>
				{:else}
					<p>{commentTeacher2InBDD}</p>
				{/if}

				{#if (userInfo.role === 'ROLE_TEACHER' || userInfo.role === 'ROLE_JURY_MEMBER' || userInfo.role === 'ROLE_PLANNING_ASSISTANT' || userInfo.role === 'ROLE_OPTION_LEADER') && (presentation.teacher[0].idUser === userInfo.userId || presentation.teacher[1].idUser === userInfo.userId)}
					<!--If it is one of the two teacher assigned to this presentation-->
					<button class="simple-btn" on:click={() => (modifCommentTeacherMode = true)}
						>Modifier</button
					>
				{/if}
			{:else}
				{#if team0or1 == 0}
					<input bind:value={commentTeacherInput1} placeholder="Entrer un commentaire" />
				{:else}
					<input bind:value={commentTeacherInput2} placeholder="Entrer un commentaire" />
				{/if}
				<div class="btn-group">
					<button class="double-btn" on:click={updateCommentTeacher}>Valider</button>
					<button class="double-btn" on:click={() => (modifCommentTeacherMode = false)}
						>Annuler</button
					>
				</div>
			{/if}
		{:else}
			<p>La présentation n'est pas encore passée</p>
		{/if}
	</div>
	<div class="rubric">
		<h3>Commentaire équipe liée</h3>
		{#if new Date() > new Date(presentation.dateHours)}
			{#if !modifCommentTeamMode}
				{#if team0or1 == 0}
					<p>{commentTeam1InBDD}</p>
				{:else}
					<p>{commentTeam2InBDD}</p>
				{/if}

				{#if userInfo.role === 'ROLE_STUDENT' || (userInfo.role === 'ROLE_TEAM_MEMBER' && presentation.teams[team0or1].number != userInfo.idTeam)}
					<button class="simple-btn" on:click={() => (modifCommentTeamMode = true)}>Modifier</button
					>
				{/if}
			{:else}
				{#if team0or1 == 0}
					<input bind:value={commentTeamInput1} placeholder="Entrer un commentaire" />
				{:else}
					<input bind:value={commentTeamInput2} placeholder="Entrer un commentaire" />
				{/if}
				<div class="btn-group">
					<button class="double-btn" on:click={updateCommentTeam}>Valider</button>
					<button class="double-btn" on:click={() => (modifCommentTeamMode = false)}>Annuler</button
					>
				</div>
			{/if}
		{:else}
			<p>La présentation n'est pas encore passée</p>
		{/if}
	</div>
	<div class="rubric">
		{#if userInfo.role === 'ROLE_TEACHER' || userInfo.role === 'ROLE_PLANNING_ASSISTANT' || userInfo.role === 'ROLE_OPTION_LEADER' || ((userInfo.role === 'ROLE_STUDENT' || userInfo.role === 'ROLE_TEAM_MEMBER') && presentation.teams[team0or1].number == userInfo.idTeam)}
			<h3>Note</h3>
			<Mark mark={presentation.teams[team0or1].markPresentation} maxMark={10} />
		{/if}
	</div>
</div>

<style lang="scss">
	.container {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		gap: 5px;

		.rubric {
			display: flex;
			flex-direction: column;
			align-items: flex-start;
			gap: 3px;

			p {
				max-width: 30ch;
			}

			.simple-btn {
				padding: 5px 10px;
				width: 100%;
				text-align: center;
				border-radius: 10px;
				color: white;
				background-color: #3366ff;
				border: none;
				font-size: 1em;
				cursor: pointer;
			}

			input {
				width: 100%;
				padding: 5px 10px;
				border-radius: 10px;
				border: 1px solid #c4c4c4;
				font-size: 1em;
			}

			.btn-group {
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				width: 100%;
				gap: 5px;

				.double-btn {
					padding: 5px 10px;
					width: calc(50% - 5px);
					text-align: center;
					border-radius: 10px;
					color: white;
					background-color: #3366ff;
					border: none;
					font-size: 1em;
					cursor: pointer;
				}
			}
		}
	}
</style>
