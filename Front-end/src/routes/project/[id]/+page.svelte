<script>
	export let data;
	import { goto } from '$app/navigation';
	import SpecificationsDownload from './SpecificationsDownload.svelte';
	import SpecificationsUpload from './SpecificationsUpload.svelte';
	import ReportDownload from './ReportDownload.svelte';
	import ReportUpload from './ReportUpload.svelte';
	import AnalysisDownload from './AnalysisDownload.svelte';
	import AnalysisUpload from './AnalysisUpload.svelte';
	import Validation from './Validation.svelte';
	import Navbar from '../../Navbar.svelte';
	import { invalidateAll } from '$app/navigation';

	import Cookies from 'js-cookie';
	const urlAPI = process.env.URLAPI;

	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: 'Bearer ' + Cookies.get('token')
	};

	$: if (typeof window !== 'undefined') {
		if (data.status === 401) {
			goto('/auth', { invalidateAll: true });
		}
	}

	$: project = data.project;
	$: linkedProject = data.linkedProject;
	$: fileLinked = data.fileLinked;
	$: team = data.team;
	$: unpairedTeam = data.unpairedTeams;
	let teamToBePairedWith;
	$: feedback = data.feedback;
	$: feedbackReport = data.feedbackReport;
	let userInfo = data.userInfo;
	$: fileCode = data.file.fileCode;
	$: reportFileCode = data.fileReport.fileCode;
	$: analysisFileCode = data.fileAnalysis.fileCode;
	$: analysisLinkedFileCode = data.analysisLinked.fileCode;
	$: reportLinkedFileCode = data.reportLinked.fileCode;
	let description;
	let name;
	let comment;
	let commentReport;
	$: modifMode = false;
	$: modifMode2 = false;
	$: modifMode3 = false;
	$: modifMode4 = false;

	$: buttonDisable = teamToBePairedWith === project.id;

	let students = data.teamStudents;
	let isInThisTeam = students.some((student) => student.id === userInfo.userId);
	let linkedStudents = data.linkedStudents;
	let linkedWithThisTeam = linkedStudents.some((student) => student.id === userInfo.userId);

	$: linkTestBook = data.team.linkTestBook;
	$: linkedTestBookLink = data.linkedTeam.linkTestBook;

	let dateModifTest;
	$: {
		if (data.team.dateModifTest) {
			const date = new Date(data.team.dateModifTest);
			const day = date.getDate().toString().padStart(2, '0');
			const month = (date.getMonth() + 1).toString().padStart(2, '0');
			const year = date.getFullYear();
			const hours = date.getHours();
			const minutes = date.getMinutes().toString().padStart(2, '0');
			dateModifTest = `Le ${day}/${month}/${year} à ${hours}h${minutes}`;
		}
	}

	function updateDescription() {
		if (description !== undefined) {
			fetch(urlAPI + '/project/updateDescription', {
				method: 'PUT',
				headers: headersList,
				body: JSON.stringify({
					id: project.id,
					description
				})
			});
			project.description = description;
			changeMode();
		} else {
			changeMode();
		}
	}

	function updateName() {
		if (name !== undefined) {
			fetch(urlAPI + '/project/updateName', {
				method: 'PUT',
				headers: headersList,
				body: JSON.stringify({
					id: project.id,
					name
				})
			});
			project.name = name;
			changeMode3();
		} else {
			changeMode3();
		}
	}

	function updateFeedback() {
		if (comment !== undefined) {
			fetch(urlAPI + `/file/${fileCode}/feedback`, {
				method: 'PUT',
				headers: headersList,
				body: JSON.stringify({
					comment
				})
			});
			feedback.comment = comment;
			changeMode2();
		} else {
			changeMode2();
		}
	}

	function updateFeedbackReport() {
		if (commentReport !== undefined) {
			fetch(urlAPI + `/file/${reportFileCode}/feedback`, {
				method: 'PUT',
				headers: headersList,
				body: JSON.stringify({
					comment: commentReport
				})
			});
			feedbackReport.comment = commentReport;
			changeMode4();
		} else {
			changeMode4();
		}
		console.log(commentReport);
	}

	function changeMode() {
		modifMode = !modifMode;
	}

	function changeMode2() {
		modifMode2 = !modifMode2;
	}

	function changeMode3() {
		modifMode3 = !modifMode3;
	}

	function changeMode4() {
		modifMode4 = !modifMode4;
	}

	// TODO: fix the window.location.reload() to refresh the page
	async function postPair() {
		const formData = {
			idLinkedTeam1: team.number,
			idLinkedTeam2: teamToBePairedWith
		};
		await fetch(urlAPI + '/optionLeader/createTeamPair', {
			method: 'POST',
			headers: headersList,
			body: JSON.stringify(formData)
		});
		invalidateAll();
	}
	let newTestBookLink;
	async function updateTestBookLink() {
		const formData = {
			teamNumber: team.number,
			linkTestBook: newTestBookLink
		};
		await fetch(urlAPI + '/teamMember/modifyTestBook', {
			method: 'PUT',
			headers: headersList,
			body: JSON.stringify(formData)
		});
		linkTestBook = newTestBookLink;
		invalidateAll();
	}
</script>

<Navbar {userInfo} />
{#if data.userInfo.role === 'ROLE_TEACHER' || userInfo.role === 'ROLE_JURY_MEMBER' || data.userInfo.role === 'ROLE_OPTION_LEADER'}
	<div class="container">
		<div class="card-project">
			<div class="project-summary">
				<div class="name-status">
					<h1>{project.name}</h1>
					<h1>Etat du projet</h1>
					<Validation {project} {userInfo} />
				</div>
				<div class="description-group">
					<h2>Description</h2>
					<p>{project.description}</p>
				</div>
				<div class="spec">
					<h2>Cahier des charges</h2>
					<SpecificationsDownload {fileCode} {project} />
				</div>
				{#if fileCode !== ''}
					{#if modifMode2}
						<div class="feedback">
							<h3>Commentaire sur le cahier des charges</h3>
							<input
								class="input-text"
								type="text"
								placeholder={feedback.comment}
								bind:value={comment}
							/>
							<button class="smallButton" on:click={updateFeedback}>Valider</button>
						</div>
					{:else if feedback.comment !== '' && feedback !== undefined}
						<div class="feedback">
							<h3>Commentaire sur le cahier des charges</h3>
							<p class="feedback">{feedback.comment}</p>
							<button class="smallButton" on:click={changeMode2}>Modifier le feedback</button>
						</div>
					{:else}
						<div class="feedback">
							<h3>Commentaire sur le cahier des charges</h3>
							<p>Pas de commentaire disponible</p>
							<button class="smallButton" on:click={changeMode2}>Modifier le feedback</button>
						</div>
					{/if}
				{/if}
				<div class="report">
					<h2>Rapport de projet</h2>
					<ReportDownload fileCode={reportFileCode} {project} />
					{#if reportFileCode !== ''}
						<h3>Ajouter des annotations</h3>
						<ReportUpload {project} />
					{/if}
				</div>
				{#if reportFileCode !== ''}
					{#if modifMode4}
						<div class="feedback">
							<h3>Commentaire sur le rapport de projet</h3>
							<input
								class="input-text"
								type="text"
								placeholder={feedbackReport.comment}
								bind:value={commentReport}
							/>
							<button class="smallButton" on:click={updateFeedbackReport}>Valider</button>
						</div>
					{:else if feedbackReport.comment !== '' && feedbackReport !== undefined}
						<div class="feedback">
							<h3>Commentaire sur le rapport de projet</h3>
							<p class="feedback">{feedbackReport.comment}</p>
							<button class="smallButton" on:click={changeMode4}>Modifier le feedback</button>
						</div>
					{:else}
						<div class="feedback">
							<h3>Commentaire sur le rapport de projet</h3>
							<p>Pas de commentaire disponible</p>
							<button class="smallButton" on:click={changeMode4}>Modifier le feedback</button>
						</div>
					{/if}
				{/if}
				<div class="analysis">
					<h2>Analyse du cahier des charges</h2>
					<AnalysisDownload fileCode={analysisFileCode} {project} />
				</div>
				<div class="test-book">
					<h2>Cahier de test</h2>
					{#if linkTestBook}
						<p><a href={linkTestBook}>{linkTestBook}</a></p>
						<p>Date de dernière modification : {dateModifTest}</p>
					{:else}
						<p>Pas de cahier de test disponible</p>
					{/if}
				</div>
			</div>
			<div class="project-composition">
				{#if students.length === 0}
					<h2>Pas d'étudiants dans l'équipe</h2>
				{:else}
					<h2>Liste des étudiants dans l'équipe</h2>
					<ul>
						{#each students as student}
							<li>
								<a href="/student/{student.id}"
									>{student.name} {student.surname} - {student.option}</a
								>
							</li>
						{/each}
					</ul>
				{/if}
			</div>
		</div>

		<div class="card-linked-project">
			{#if linkedProject.name === undefined}
				<h2>Pas de projet lié</h2>
				{#if userInfo.role === 'ROLE_OPTION_LEADER'}
					<h3>Lier avec une équipe</h3>
					<select id="teamInput" bind:value={teamToBePairedWith}>
						<option disabled>Choisir une équipe</option>
						{#each unpairedTeam as teamm}
							{#if teamm.number != team.number}
								<option value={teamm.number}>Équipe n°{teamm.number}</option>
							{/if}
						{/each}
					</select>
					<button id="buttonCreate" on:click={postPair} disabled={buttonDisable}
						>Créer la paire</button
					>
				{/if}
			{:else}
				<div class="project-summary">
					{#if linkedProject.name !== undefined}
						<div class="name-status">
							<h1>Projet lié : {linkedProject.name}</h1>
							{#if linkedProject.status === 'VALIDATED'}
								<p>Validé</p>
							{:else if linkedProject.status === 'REFUSED'}
								<p>Refusé</p>
							{:else}
								<p>En attente de validation</p>
							{/if}
						</div>
						<div class="description-group">
							<h2>Description</h2>
							<p>{linkedProject.description}</p>
						</div>
						<div class="spec">
							<h2>Cahier des charges</h2>
							<SpecificationsDownload fileCode={fileLinked.fileCode} project={linkedProject} />
						</div>
						<div class="report">
							<h2>Rapport de projet</h2>
							<ReportDownload fileCode={reportLinkedFileCode} project={linkedProject} />
						</div>
						<div class="analysis">
							<h2>Analyse du cahier des charges</h2>
							<AnalysisDownload fileCode={analysisLinkedFileCode} project={linkedProject} />
						</div>
						<div class="test-book">
							<h2>Cahier de test</h2>
							{#if linkTestBook}
								<p><a href={linkTestBook}>{linkTestBook}</a></p>
								<p>Date de dernière modification : {dateModifTest}</p>
							{:else}
								<p>Pas de cahier de test disponible</p>
							{/if}
						</div>
					{/if}
				</div>
				<div class="project-linked-composition">
					{#if !(linkedProject.name === undefined)}
						{#if linkedStudents.length === 0}
							<h2>Pas d'étudiants dans l'équipe</h2>
						{:else}
							<h2>Liste des étudiants dans l'équipe</h2>
							<ul>
								{#each linkedStudents as student}
									<li>
										<a href="/student/{student.id}"
											>{student.name} {student.surname} - {student.option}</a
										>
									</li>
								{/each}
							</ul>
						{/if}
					{/if}
				</div>
			{/if}
		</div>
	</div>
{:else if data.userInfo.role === 'ROLE_TEAM_MEMBER' && isInThisTeam}
	<div class="container">
		<div class="card-project">
			<div class="project-summary">
				{#if modifMode3}
					<div class="name-status">
						<input class="input-text" type="text" placeholder={project.name} bind:value={name} />
						<button class="smallButton" on:click={updateName}>Valider</button>

						<h1>Etat du projet</h1>
						<Validation {project} {userInfo} />
					</div>
				{:else}
					<div class="name-status">
						<h1>{project.name}</h1>
						<button class="smallButton" on:click={changeMode3}>Modifier</button>

						<h1>Etat du projet</h1>
						<Validation {project} {userInfo} />
					</div>
				{/if}
				{#if modifMode}
					<div class="description-group">
						<h2>Description</h2>
						<input
							class="input-text"
							type="text"
							placeholder={project.description}
							bind:value={description}
						/>
						<button class="smallButton" on:click={updateDescription}>Valider</button>
					</div>
				{:else}
					<div class="description-group">
						<h2>Description</h2>
						<p>{project.description}</p>
						<button class="smallButton" on:click={changeMode}>Modifier</button>
					</div>
				{/if}
				<div class="spec">
					<h2>Cahier des charges</h2>
					<SpecificationsUpload {project} />
					<SpecificationsDownload {fileCode} {project} />
				</div>
				<div class="feedback">
					{#if fileCode !== ''}
						{#if feedback.comment !== '' && feedback !== undefined}
							<h2>Commentaire sur le cahier des charges</h2>
							<p class="feedback">{feedback.comment}</p>
						{:else}
							<h2>Commentaire sur le cahier des charges</h2>
							<p>Pas de commentaire disponible</p>
						{/if}
					{/if}
				</div>
				<div class="report">
					<h2>Rapport de projet</h2>
					<ReportUpload {project} />
					<ReportDownload fileCode={reportFileCode} {project} />
				</div>
				<div class="feedback">
					{#if reportFileCode !== ''}
						{#if feedbackReport.comment !== '' && feedbackReport !== undefined}
							<h2>Commentaire sur le rapport de projet</h2>
							<p class="feedback">{feedbackReport.comment}</p>
						{:else}
							<h2>Commentaire sur le rapport de projet</h2>
							<p>Pas de commentaire disponible</p>
						{/if}
					{/if}
				</div>
				<div class="analysis">
					<h2>Analyse du cahier des charges</h2>
					<AnalysisDownload fileCode={analysisFileCode} {project} />
				</div>
				<div class="test-book">
					<h2>Cahier de test</h2>
					{#if linkTestBook}
						<p><a href={linkTestBook}>{linkTestBook}</a></p>
						<p>Date de dernière modification : {dateModifTest}</p>
					{:else}
						<p>Pas de cahier de test disponible</p>
					{/if}
				</div>
				<div class="test-book-add">
					<h2>Ajouter / modifier le lien vers le cahier de test</h2>
					<form on:submit|preventDefault={updateTestBookLink}>
						<input bind:value={newTestBookLink} placeholder="Nouveau lien vers le cahier de test" />
						<button class="smallButton" type="submit">Mettre à jour</button>
					</form>
				</div>
			</div>
			<div class="project-composition">
				{#if students.length === 0}
					<h2>Pas d'étudiants dans l'équipe</h2>
				{:else}
					<h2>Liste des étudiants dans l'équipe</h2>
					<ul>
						{#each students as student}
							<li>{student.name} {student.surname} - {student.option}</li>
						{/each}
					</ul>
				{/if}
			</div>
		</div>

		<div class="card-linked-project">
			{#if linkedProject.name === undefined}
				<h2>Pas de projet lié</h2>
			{:else}
				<div class="project-summary">
					{#if linkedProject.name !== undefined}
						<div class="name-status">
							<h1>Projet lié : {linkedProject.name}</h1>
							{#if linkedProject.status === 'VALIDATED'}
								<p>Validé</p>
							{:else if linkedProject.status === 'REFUSED'}
								<p>Refusé</p>
							{:else}
								<p>En attente de validation</p>
							{/if}
						</div>
						<div class="description-group">
							<h2>Description</h2>
							<p>{linkedProject.description}</p>
						</div>
						<div class="spec">
							<h2>Cahier des charges</h2>
							<SpecificationsDownload fileCode={fileLinked.fileCode} project={linkedProject} />
						</div>
						<div class="analysis">
							<h2>Analyse du cahier des charges</h2>
							<AnalysisUpload project={linkedProject} />
							<AnalysisDownload fileCode={analysisLinkedFileCode} project={linkedProject} />
						</div>
						<div class="test-book">
							<h2>Cahier de test</h2>
							{#if linkTestBook}
								<p><a href={linkedTestBookLink}>{linkedTestBookLink}</a></p>
							{:else}
								<p>Pas de cahier de test disponible</p>
							{/if}
						</div>
					{/if}
				</div>
				<div class="project-linked-composition">
					{#if !(linkedProject.name === undefined)}
						{#if linkedStudents.length === 0}
							<h2>Pas d'étudiants dans l'équipe</h2>
						{:else}
							<h2>Liste des étudiants dans l'équipe</h2>
							<ul>
								{#each linkedStudents as student}
									<li>{student.name} {student.surname} - {student.option}</li>
								{/each}
							</ul>
						{/if}
					{/if}
				</div>
			{/if}
		</div>
	</div>
{:else if (data.userInfo.role === 'ROLE_STUDENT' || data.userInfo.role === 'ROLE_TEAM_MEMBER') && linkedWithThisTeam}
	<div class="container">
		<div class="card-project">
			<div class="project-summary">
				<div class="name-status">
					<h1>{project.name}</h1>

					<h1>Etat du projet</h1>
					<Validation {project} {userInfo} />
				</div>
				<div class="description-group">
					<h2>Description</h2>
					<p>{project.description}</p>
				</div>
				<div class="spec">
					<h2>Cahier des charges</h2>
					<SpecificationsDownload {fileCode} {project} />
				</div>
				<div class="feedback">
					{#if fileCode !== ''}
						<h2>Commentaire sur le cahier des charges</h2>
						{#if feedback.comment !== '' && feedback !== undefined}
							<p class="feedback">{feedback}</p>
						{:else}
							<p>Pas de commentaire disponible</p>
						{/if}
					{/if}
				</div>
				<div class="analysis">
					<h2>Analyse du cahier des charges</h2>
					<AnalysisUpload {project} />
					<AnalysisDownload fileCode={analysisFileCode} {project} />
				</div>
				<div class="test-book">
					<h2>Cahier de test</h2>
					{#if linkTestBook}
						<p><a href={linkTestBook}>{linkTestBook}</a></p>
						<p>Date de dernière modification : {dateModifTest}</p>
					{:else}
						<p>Pas de cahier de test disponible</p>
					{/if}
				</div>
			</div>
			<div class="project-composition">
				{#if students.length === 0}
					<h2>Pas d'étudiants dans l'équipe</h2>
				{:else}
					<h2>Liste des étudiants dans l'équipe</h2>
					<ul>
						{#each students as student}
							<li>{student.name} {student.surname} - {student.option}</li>
						{/each}
					</ul>
				{/if}
			</div>
		</div>

		<div class="card-linked-project">
			{#if linkedProject.name === undefined}
				<h2>Pas de projet lié</h2>
			{:else}
				<div class="project-summary">
					{#if linkedProject.name !== undefined}
						<div class="name-status">
							<h1>Projet lié : {linkedProject.name}</h1>
							{#if linkedProject.status === 'VALIDATED'}
								<p>Validé</p>
							{:else if linkedProject.status === 'REFUSED'}
								<p>Refusé</p>
							{:else}
								<p>En attente de validation</p>
							{/if}
						</div>
						<div class="description-group">
							<h2>Description</h2>
							<p>{linkedProject.description}</p>
						</div>
						<div class="spec">
							<h2>Cahier des charges</h2>
							<SpecificationsDownload fileCode={fileLinked.fileCode} project={linkedProject} />
						</div>
						<div class="test-book">
							<h2>Cahier de test</h2>
							{#if linkTestBook}
								<p><a href={linkedTestBookLink}>{linkedTestBookLink}</a></p>
							{:else}
								<p>Pas de cahier de test disponible</p>
							{/if}
						</div>
					{/if}
				</div>
				<div class="project-linked-composition">
					{#if !(linkedProject.name === undefined)}
						{#if linkedStudents.length === 0}
							<h2>Pas d'étudiants dans l'équipe</h2>
						{:else}
							<h2>Liste des étudiants dans l'équipe</h2>
							<ul>
								{#each linkedStudents as student}
									<li>{student.name} {student.surname} - {student.option}</li>
								{/each}
							</ul>
						{/if}
					{/if}
				</div>
			{/if}
		</div>
	</div>
{:else}
	<div class="container">
		<div class="card-project">
			<div class="project-summary">
				<h2>{project.name}</h2>
				<p>Description : {project.description}</p>
			</div>
			<div class="project-composition">
				{#if students.length === 0}
					<h2>Pas d'étudiants dans l'équipe</h2>
				{:else}
					<h2>Liste des étudiants dans l'équipe</h2>
					<ul>
						{#each students as student}
							<li>{student.name} {student.surname} - {student.option}</li>
						{/each}
					</ul>
				{/if}
			</div>
		</div>

		<div class="card-linked-project">
			{#if linkedProject.name === undefined}
				<h2>Pas de projet lié</h2>
			{:else}
				<div class="project-linked-summary">
					{#if linkedProject.name !== undefined}
						<h2>Projet lié : {linkedProject.name}</h2>
						{#if linkedProject.status === 'VALIDATED'}
							<p>Validé</p>
						{:else if linkedProject.status === 'REFUSED'}
							<p>Refusé</p>
						{:else}
							<p>En attente de validation</p>
						{/if}
						<p>Description : {linkedProject.description}</p>
					{/if}
				</div>
				<div class="project-linked-composition">
					{#if !(linkedProject.name === undefined)}
						{#if linkedStudents.length === 0}
							<h2>Pas d'étudiants dans l'équipe</h2>
						{:else}
							<h2>Liste des étudiants dans l'équipe</h2>
							<ul>
								{#each linkedStudents as student}
									<li>{student.name} {student.surname} - {student.option}</li>
								{/each}
							</ul>
						{/if}
					{/if}
				</div>
			{/if}
		</div>
	</div>
{/if}

<style lang="scss">
	.container {
		margin: 50px 0;
		.card-project {
			display: flex;
			flex-direction: row;
			justify-content: space-around;
			align-items: center;
			border-radius: 50px;
			background-color: white;
			color: black;
			padding: 2% 10% 4% 10%;
			margin: 0 10% 3% 10%;
			box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
			gap: 10px;

			.project-summary {
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				gap: 15px;
				width: 100%;

				.name-status {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;

					input {
						padding: 10px;
						border-radius: 10px;
						width: 100%;
						border: 1px solid #c4c4c4;
					}
				}

				.description-group {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;

					p {
						display: block;
						overflow: hidden;
					}

					input {
						padding: 10px;
						border-radius: 10px;
						width: 100%;
						border: 1px solid #c4c4c4;
					}
				}

				.spec {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;
				}

				.report {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;
				}

				.analysis {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;
				}

				.feedback {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;

					button {
						width: 100%;
						color: white;
						background-color: #3366ff;
						border: none;
						font-size: 1em;
					}

					input {
						padding: 10px;
						border-radius: 10px;
						width: 100%;
						border: 1px solid #c4c4c4;
					}
				}
			}
		}

		.card-linked-project {
			display: flex;
			flex-direction: row;
			justify-content: space-around;
			align-items: center;
			border-radius: 50px;
			background-color: white;
			color: black;
			padding: 2% 10% 4% 10%;
			margin: 0 10% 3% 10%;
			box-shadow: 0px 20px 50px rgba(0, 0, 0, 0.25);
			gap: 10px;

			.project-summary {
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				gap: 15px;
				width: 100%;

				.name-status {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;
				}

				.description-group {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;
				}

				.spec {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;
				}

				.analysis {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;
				}

				.report {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					gap: 5px;
					width: 100%;
				}
			}
		}
	}

	#buttonCreate {
		padding: 2% 2%;
		text-align: center;
		border-radius: 10px;
		width: 30%;
		color: white;
		background-color: #3366ff;
		border: none;
		font-size: 1em;
		cursor: pointer;
	}

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

	.smallButton:hover {
		background-color: #7144ec;
		cursor: pointer;
		transform: translateY(-2px);
	}

	.project-composition {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 100%;
		gap: 10px;

		ul > li > a {
			color: black;
			text-decoration: none;
			text-decoration: underline;
		}
	}

	.project-linked-composition {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		width: 100%;
		gap: 10px;

		li > a {
			color: black;
			text-decoration: none;
			text-decoration: underline;
		}
	}

	.test-book {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: flex-start;
		gap: 15px;
		align-self: flex-start;

		p {
			overflow: hidden;
			display: block;

			a {
				display: block;
				padding: 10px;
				text-align: center;
				border-radius: 10px;
				color: white;
				background-color: #6844ec;
				border: none;
				font-size: 1em;
				overflow: hidden;
				text-decoration: none;

				&:hover {
					background-color: #a245e0;
					text-decoration: underline;
				}
			}
		}
	}

	.test-book-add {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: flex-start;
		gap: 15px;
		width: 100%;

		input {
			padding: 10px;
			border-radius: 10px;
			width: 100%;
			border: 1px solid #c4c4c4;
		}

		form {
			display: flex;
			flex-direction: row;
			justify-content: center;
			align-items: flex-start;
			gap: 15px;
			width: 100%;
		}

		button {
			padding: 10px;
			text-align: center;
			border-radius: 10px;
			width: 50%;
			color: white;
			background-color: #3366ff;
			border: none;
			font-size: 1em;

			&:hover {
				background-color: #7144ec;
				cursor: pointer;
				transform: translateY(-2px);
			}
		}
	}
</style>
