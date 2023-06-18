<script>
	export let presentation;
	import Cookies from 'js-cookie';
	const urlAPI = process.env.URLAPI;

	$: commentTeacherInBDD = presentation.commentTeacher;
	let commentTeacherInput = presentation.commentTeacher;

	let modifyCommentTeacherMode = false;

	function toggleCommentTeacherMode() {
		modifyCommentTeacherMode = !modifyCommentTeacherMode;
		commentTeacherInput = commentTeacherInBDD;
	}

	async function sendCommentTeacherToDB() {
		console.log('sendCommentTeacherToDB');
		await fetch(urlAPI + `/teacher/setPresentationCommentTeacher`, {
			method: 'PUT',
			headers: {
				'Content-Type': 'application/json',
				Authorization: 'Bearer ' + Cookies.get('token')
			},
			body: JSON.stringify({
				idPresentation: presentation.idPresentation,
				commentTeacher: commentTeacherInput
			})
		});
		commentTeacherInBDD = commentTeacherInput; // Maybe check if the request was successful
		toggleCommentTeacherMode();
	}
</script>

<div class="container">
	{#if !modifyCommentTeacherMode}
		<la>{commentTeacherInBDD}</la>
		<button on:click={toggleCommentTeacherMode}>Modifier</button>
	{:else}
		<textarea type="text" placeholder={commentTeacherInBDD} bind:value={commentTeacherInput} />
		<button on:click={sendCommentTeacherToDB}>Valider</button>
		<button on:click={toggleCommentTeacherMode}>Annuler</button>
	{/if}
</div>
