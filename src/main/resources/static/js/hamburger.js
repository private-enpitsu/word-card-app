/**
 * ハンバーガーメニュー用
 */
{
	document.addEventListener('DOMContentLoaded', () => {
	  const nav  = document.getElementById('navArea');
	  const btns = document.querySelectorAll('.toggle_btn');
	  const mask = document.getElementById('mask');
	  const openClass = 'open';

	  // menu open close
	  btns.forEach((btn) => {
	    btn.addEventListener('click', (e) => {
	      // 元コードは return false してないので基本不要。リンクなら止めたい場合は下を有効化
	      // e.preventDefault();
	      nav.classList.toggle(openClass);
	    });
	  });

	  // mask close
	  if (mask) {
	    mask.addEventListener('click', () => {
	      nav.classList.remove(openClass);
	    });
	  }
	});

}