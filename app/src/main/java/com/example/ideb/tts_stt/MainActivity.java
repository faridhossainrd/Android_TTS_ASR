package com.example.ideb.tts_stt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button tts,stt;
    TextView rdtxt;
    EditText wrtxt;
    TextToSpeech myTTS;
    SpeechRecognizer mySTT;
    boolean firstent=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts=(Button)findViewById(R.id.TTS);
        stt=(Button)findViewById(R.id.button2);
        rdtxt=(TextView)findViewById(R.id.textView);
        wrtxt=(EditText)findViewById(R.id.editText);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            doPermAudio();
        }
        init_TTS_SST();
        init_STT();
        rdtxt.setText("");
        rdtxt.append("you can asked me like");
        rdtxt.append("\n\r 1. what is your name");
        rdtxt.append("\n\r 2. what time is it");
        rdtxt.append("\n\r 3. open browser");
        rdtxt.append("\n\r 4. or other(but other not give your ans");
        wrtxt.setText("write here to spoken by machine");

        tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt=wrtxt.getText().toString();
                if(txt!=null)
                {
                    speak(txt );
                }
                else
                {
                    speak("please first enter something in the text box");
                }

            }
        });


        stt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstent)
                {
                    speak("OK  recognizer ready , you can speak now");
                    while (myTTS.isSpeaking());
                    firstent=false;
                }


                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySTT.startListening(intent);

            }
        });



    }

    void doPermAudio()
    {
        int MY_PERMISSIONS_RECORD_AUDIO = 1;
        MainActivity thisActivity = this;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_RECORD_AUDIO);
        }
    }

    void init_TTS_SST()
    {
        myTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0)
                {
                    Toast.makeText(MainActivity.this,"there is no speech engines",Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    myTTS.setLanguage(Locale.US);
                    Toast.makeText(MainActivity.this,"OK speech engines is ready",Toast.LENGTH_LONG).show();

                }
            }
        });
    }



  void speak(String massge )
  {
      if(Build.VERSION.SDK_INT>=21)
      {
          myTTS.speak(massge,TextToSpeech.QUEUE_FLUSH,null,null);
      }
      else
      {
          myTTS.speak(massge,TextToSpeech.QUEUE_FLUSH,null);
      }
  }

    void init_STT()
    {
           if(SpeechRecognizer.isRecognitionAvailable(this))
           {
               mySTT=SpeechRecognizer.createSpeechRecognizer(this);
               mySTT.setRecognitionListener(new RecognitionListener() {
                   @Override
                   public void onReadyForSpeech(Bundle params) {

                   }

                   @Override
                   public void onBeginningOfSpeech() {

                   }

                   @Override
                   public void onRmsChanged(float rmsdB) {

                   }

                   @Override
                   public void onBufferReceived(byte[] buffer) {

                   }

                   @Override
                   public void onEndOfSpeech() {

                   }

                   @Override
                   public void onError(int error) {
                       String ss=Integer.valueOf(error).toString();
                       Toast.makeText(MainActivity.this,"please try again",Toast.LENGTH_LONG).show();
                   }

                   @Override
                   public void onResults(Bundle results) {

                       List<String> result=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                       process(result.get(0));
                   }

                   @Override
                   public void onPartialResults(Bundle partialResults) {

                   }

                   @Override
                   public void onEvent(int eventType, Bundle params) {

                   }
               });
           }
           else
           {
               Toast.makeText(MainActivity.this,"not available speech recognition",Toast.LENGTH_LONG).show();
           }
    }

    private void process(String cmd) {

         cmd=cmd.toLowerCase();
        rdtxt.setText(cmd);
        rdtxt.append("\n\r you can asked me like");
        rdtxt.append("\n\r 1. what is your name");
        rdtxt.append("\n\r 2. what time is it");
        rdtxt.append("\n\r 3. open browser");
        rdtxt.append("\n\r 4. or other(but other not give your ans");
        wrtxt.setText("write here to spoken by machine");

           if(cmd.indexOf("what")!=-1)
           {
               if(cmd.indexOf("your name")!=-1)
               {
                   speak("my name is . is lin") ;
               }
               if(cmd.indexOf("time")!=-1)
               {
                   Date date=new Date();
                   String time=DateUtils.formatDateTime(this,date.getTime(),DateUtils.FORMAT_SHOW_TIME);
                   speak("the time now is "+time);
               }

           }
        else if(cmd.indexOf("open")!=-1)
        {
            if(cmd.indexOf("browser")!=-1)
            {

                 Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.kalerkantho.com/print-edition/techbishwa/09/29/6852" +
                          "55/685255?fbclid=IwAR0mUiYI9HboKb4yvwR48CFNfvsOP1W3oDfDjD3nt_8DeYlE8gPYQS6Mw0o"));
                  startActivity(intent);

            }
        }

    }


}
